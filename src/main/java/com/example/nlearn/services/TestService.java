package com.example.nlearn.services;

import com.example.nlearn.dtos.FullTestDto;
import com.example.nlearn.dtos.MarkDto;
import com.example.nlearn.dtos.TestDto;
import com.example.nlearn.dtos.UserDto;
import com.example.nlearn.models.Mark;
import com.example.nlearn.models.Test;
import com.example.nlearn.models.User;
import com.example.nlearn.records.StudentsContent;
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
    private final UserService userService;
    private final MarkService markService;
    private final ModelMapper modelMapper;

    public TestService(TestRepository testRepository, UserService userService, MarkService markService, ModelMapper modelMapper) {
        this.testRepository = testRepository;
        this.userService = userService;
        this.markService = markService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    private boolean userIsOwner(Test test, int userId) {
        return test.getAuthor().getId() == userId;
    }

    public ResponseEntity deleteTest(Integer testId, Integer userId, Boolean isAdmin) {
        Test test = testRepository.getTestById(testId);
        if (!(isAdmin || userIsOwner(test, userId))) {
            return ResponseEntity.notFound().build();
        }

        testRepository.deleteById(testId);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity createTest(FullTestDto fullTestDto, User creator) {
        Test test = modelMapper.map(fullTestDto, Test.class);
        test.setAuthor(userService.getUser(creator.getId()));
        testRepository.save(test);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity updateTest(FullTestDto fullTestDto, Integer userId, Boolean isAdmin) {
        Test test = testRepository.getById(fullTestDto.getId());
        if (!(isAdmin || userIsOwner(test, userId))) {
            return ResponseEntity.notFound().build();
        }

        Test updatedTest = modelMapper.map(fullTestDto, Test.class);
        updatedTest.setAuthor(test.getAuthor());
        testRepository.save(updatedTest);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public FullTestDto getTest(Integer testId, User user, Boolean isAdmin) {
        Test test = testRepository.getById(testId);
        if (!(isAdmin || userIsOwner(test, user.getId()))) {
            throw new ResourceAccessException("");
        }

        return modelMapper.map(test, FullTestDto.class);
    }

    public List<TestDto> getTop5Tests(int teacherId) {
        return mapList(testRepository.findTop5ByAuthorIdOrderByIdDesc(teacherId), TestDto.class);
    }

    public List<TestDto> getAllTests(int teacherId) {
        return mapList(testRepository.findAllByAuthorIdOrderByIdDesc(teacherId), TestDto.class);
    }

    @Transactional
    public TestResults getTestResults(Integer testId, User user, Boolean isAdmin) {
        Test test = testRepository.getById(testId);
        if (!(isAdmin || userIsOwner(test, user.getId()))) {
            throw new ResourceAccessException("");
        }

        List<Mark> marks = markService.findByTestId(testId);
        List<User> users = userService.findAllByGroupId(test.getGroup());
        return new TestResults(mapList(users, UserDto.class), mapList(marks, MarkDto.class));
    }

    public Test getTestById(Integer id) {
        return testRepository.getById(id);
    }

    @Transactional
    public StudentsContent getContentForStudent(User user) {
        user = userService.getUser(user.getId());
        List<Mark> marks = markService.getAllByUser(user);
        List<Test> tests = testRepository.findByGroupInOrderByIdDesc(user.getGroups());
        return new StudentsContent(mapList(tests, TestDto.class), mapList(marks, MarkDto.class));
    }

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream().map(element -> modelMapper.map(element, targetClass)).toList();
    }
}
