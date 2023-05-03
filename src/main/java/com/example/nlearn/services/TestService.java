package com.example.nlearn.services;

import com.example.nlearn.models.FullTest;
import com.example.nlearn.models.Group;
import com.example.nlearn.models.Question;
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


    public ResponseEntity deleteTest(Integer testId, Integer userId, Boolean isAdmin) {

        if (!(isAdmin || testRepository.getById(testId).getAuthor().getId() == userId))
            return ResponseEntity.notFound().build();

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

        if (!(isAdmin || testRepository.getById(testId).getAuthor().getId() == userId))
            return ResponseEntity.notFound().build();

        Test finalTest;
        Test test = testRepository.findById(testId).orElseThrow(RuntimeException::new);
        Test receivedTest = fullTest.test();
        List<Question> receivedQuestions = fullTest.questions();

        List<Integer> createdQuestions = fullTest.addedIds();
        List<Integer> deletedQuestions = fullTest.deletedIds();

        test.setName(receivedTest.getName());
        test.setDuration(receivedTest.getDuration());
        test.setGroup(receivedTest.getGroup());
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
                questionService.save(oldQuestion);
            }
        });

        return ResponseEntity.ok(test);
    }


    public FullTest getTest(Integer testId, User author, Boolean isAdmin) {
        Test test = testRepository.getTestById(testId);
        if (!(isAdmin || test.getAuthor().getId() == author.getId())) throw new ResourceAccessException("");
        List<Question> questions = questionService.findByTestId(testId);
        List<Group> groups = groupService.getGroups();
        return new FullTest(test, questions, groups, null, null);
    }

    public List<Test> getTop5Tests(int teacherId) {
        return testRepository.findTop5ByAuthorIdOrderByIdDesc(teacherId);
    }


    public List<Test> getTop5TestsForStudent(int groupId) {
        return testRepository.findTop5ByGroupIdOrderByIdDesc(groupId);
    }
}
