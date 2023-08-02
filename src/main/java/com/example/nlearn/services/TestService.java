package com.example.nlearn.services;

import com.example.nlearn.models.FullTest;
import com.example.nlearn.models.Group;
import com.example.nlearn.models.Mark;
import com.example.nlearn.models.Question;
import com.example.nlearn.models.StudentsContent;
import com.example.nlearn.models.Test;
import com.example.nlearn.models.User;
import com.example.nlearn.repos.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
@CrossOrigin
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarkService markService;

    public ResponseEntity deleteTest(Integer testId, Integer userId, Boolean isAdmin) {

        boolean userIsOwner = testRepository.getById(testId).getAuthor().getId() == userId;
        if (!(isAdmin || userIsOwner)) {
            return ResponseEntity.notFound().build();
        }

        questionService.deleteAllByTestId(testId);
        testRepository.deleteById(testId);

        return ResponseEntity.ok().build();
    }


    public ResponseEntity createTest(FullTest fullTest, User creator) {

        Test test = fullTest.test();
        List<Question> questions = fullTest.questions();

        test.setAuthor(userService.getById(creator.getId()));
        test = testRepository.save(test);

        Test finalTest = test;
        questions.stream().forEach(q -> {
            q.setTest(finalTest);
            questionService.save(q);
        });

        return ResponseEntity.ok().build();
    }


    public ResponseEntity updateTest(Integer testId, FullTest fullTest, Integer userId, Boolean isAdmin) {
        Test test = testRepository.getTestById(testId);
        boolean userIsOwner = test.getAuthor().getId() == userId;
        if (!(isAdmin || userIsOwner)) {
            return ResponseEntity.notFound().build();
        }

        Test finalTest;
        Test receivedTest = fullTest.test();
        List<Question> receivedQuestions = fullTest.questions();

        List<Integer> createdQuestions = fullTest.addedIds();
        List<Integer> deletedQuestions = fullTest.deletedIds();

        test.setName(receivedTest.getName());
        test.setDuration(receivedTest.getDuration());
        test.setGroup(receivedTest.getGroup());
        test.setNumberOfRetries(receivedTest.getNumberOfRetries());
        test.setEndTime(receivedTest.getEndTime());
        test = testRepository.save(test);
        finalTest = test;

        deletedQuestions.forEach(i -> questionService.deleteById(i));//TODO add if(exist)

        receivedQuestions.stream().forEach(q -> {
            if (createdQuestions.contains(q.getId())) {
                q.setTest(finalTest);
                questionService.save(q);
            } else {
                Question oldQuestion = questionService.getById(q.getId());
                oldQuestion.setCorrectIndexes(q.getCorrectIndexes());
                oldQuestion.setQuestionText(q.getQuestionText());
                oldQuestion.setAnswerVariants(q.getAnswerVariants());
                oldQuestion.setNumberOfCorrectAnswers(q.getNumberOfCorrectAnswers());
                questionService.save(oldQuestion);
            }
        });

        return ResponseEntity.ok(test);
    }


    public FullTest getTest(Integer testId, User user, Boolean isAdmin) {
        Test test = testRepository.getTestById(testId);
        boolean userIsOwner = test.getAuthor().getId() == user.getId();
        if (!(isAdmin || userIsOwner)) {
            throw new ResourceAccessException("");
        }

        List<Question> questions = questionService.findByTestId(testId);
        List<Group> groups = groupService.getGroups();
        return new FullTest(test, questions, groups, null, null);
    }

    public List<Test> getTop5Tests(int teacherId) {
        return testRepository.findTop5ByAuthorIdOrderByIdDesc(teacherId);
    }

    public Test getTestById(Integer id) { return testRepository.getTestById(id); }

    public StudentsContent getContentForStudent(User user) {
        List<Mark> marks = markService.getAllByUser(user);
        List<Test> tests = testRepository.getByGroupIdOrderByIdDesc(user.getGroup().getId());
        return new StudentsContent(tests, marks);
    }
}
