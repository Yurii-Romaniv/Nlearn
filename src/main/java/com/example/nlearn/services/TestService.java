package com.example.nlearn.services;

import com.example.nlearn.dtos.MarkDto;
import com.example.nlearn.dtos.TestDto;
import com.example.nlearn.records.FullTest;
import com.example.nlearn.models.Group;
import com.example.nlearn.models.Mark;
import com.example.nlearn.models.Question;
import com.example.nlearn.records.StudentsContent;
import com.example.nlearn.models.Test;
import com.example.nlearn.models.User;
import com.example.nlearn.records.TestResults;
import com.example.nlearn.repos.TestRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
@CrossOrigin
public class TestService {
    private final TestRepository testRepository;
    private final QuestionService questionService;
    private final GroupService groupService;
    private final UserService userService;
    private final MarkService markService;
    private final ModelMapper modelMapper;

    public TestService(TestRepository testRepository, QuestionService questionService, GroupService groupService, UserService userService, MarkService markService, ModelMapper modelMapper) {
        this.testRepository = testRepository;
        this.questionService = questionService;
        this.groupService = groupService;
        this.userService = userService;
        this.markService = markService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ResponseEntity deleteTest(Integer testId, Integer userId, Boolean isAdmin) {

        boolean userIsOwner = testRepository.getTestById(testId).getAuthor().getId() == userId;
        if (!(isAdmin || userIsOwner)) {
            return ResponseEntity.notFound().build();
        }

        questionService.deleteAllByTestId(testId);
        markService.deleteAllByTestId(testId);
        testRepository.deleteById(testId);

        return ResponseEntity.ok().build();
    }


    public ResponseEntity createTest(FullTest fullTest, User creator) {

        Test test = modelMapper.map(fullTest.test(), Test.class);
        List<Question> questions = fullTest.questions();

        test.setAuthor(userService.getById(creator.getId()));
        test = testRepository.save(test);

        Test finalTest = test;
        questions.forEach(q -> {
            q.setTest(finalTest);
            questionService.save(q);
        });

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity updateTest(Integer testId, FullTest fullTest, Integer userId, Boolean isAdmin) {
        Test test = testRepository.getTestById(testId);
        boolean userIsOwner = test.getAuthor().getId() == userId;
        if (!(isAdmin || userIsOwner)) {
            return ResponseEntity.notFound().build();
        }

        Test finalTest;
        TestDto receivedTest = fullTest.test();
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

        receivedQuestions.forEach(q -> {
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

        return ResponseEntity.ok(modelMapper.map(test, TestDto.class));
    }


    public FullTest getTest(Integer testId, User user, Boolean isAdmin) {
        Test test = testRepository.getTestById(testId);
        boolean userIsOwner = test.getAuthor().getId() == user.getId();
        if (!(isAdmin || userIsOwner)) {
            throw new ResourceAccessException("");
        }

        List<Question> questions = questionService.findByTestId(testId);
        List<Group> groups = groupService.getGroups();
        return new FullTest(modelMapper.map(test, TestDto.class), questions, groups, null, null);
    }

    public List<TestDto> getTop5Tests(int teacherId) {
        return mapList(testRepository.findTop5ByAuthorIdOrderByIdDesc(teacherId), TestDto.class);
    }

    public List<TestDto> getAllTests(int teacherId) {
        return mapList(testRepository.findAllByAuthorIdOrderByIdDesc(teacherId), TestDto.class);
    }

    public TestResults getTestResults(Integer testId, User user, Boolean isAdmin) {
        Test test = testRepository.getTestById(testId);
        boolean userIsOwner = test.getAuthor().getId() == user.getId();
        if (!(isAdmin || userIsOwner)) {
            throw new ResourceAccessException("");
        }

        List<Mark> marks = markService.findByTestId(testId);
        return new TestResults(userService.findAllByGroupId(test.getGroup().getId()), mapList(marks, MarkDto.class));
    }

    public Test getTestById(Integer id) { return testRepository.getTestById(id); }

    public StudentsContent getContentForStudent(User user) {
        List<Mark> marks = markService.getAllByUser(user);
        List<Test> tests = testRepository.getByGroupIdOrderByIdDesc(user.getGroup().getId());
        return new StudentsContent(mapList(tests, TestDto.class), mapList(marks, MarkDto.class) );
    }

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream().map(element -> modelMapper.map(element, targetClass)).toList();
    }
}
