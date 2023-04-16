package com.example.nlearn.controllers;

import com.example.nlearn.models.FullTest;
import com.example.nlearn.models.Question;
import com.example.nlearn.models.Test;

import com.example.nlearn.repos.GroupRepository;
import com.example.nlearn.repos.QuestionRepository;
import com.example.nlearn.repos.TestRepository;
import com.example.nlearn.repos.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Integer id) {
        questionRepository.deleteAllByTestId(id);
        testRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }


    @PostMapping("/new")
    public ResponseEntity createTest(@RequestBody FullTest fullTest) {

        int teacherId = 3; //TODO get id from auth

        Test test = fullTest.test();
        List<Question> questions = fullTest.questions();

        test.setAuthor(userRepository.getById(teacherId));
        test.setGroup(groupRepository.getByName(fullTest.groupName()));
        test = testRepository.save(test);

        Test finalTest = test;
        questions.stream().forEach(q -> {
            q.setTest(finalTest);
            questionRepository.save(q);
        });

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTest(@PathVariable Integer id, @RequestBody FullTest fullTest) {
        Test finalTest;
        Test test = testRepository.findById(id).orElseThrow(RuntimeException::new);
        Test receivedTest = fullTest.test();
        List<Question> receivedQuestions = fullTest.questions();

        List<Integer> createdQuestions = fullTest.addedIds();
        List<Integer> deletedQuestions = fullTest.deletedIds();

        test.setName(receivedTest.getName());
        test.setDuration(receivedTest.getDuration());
        test.setGroup(groupRepository.getByName(fullTest.groupName()));
        test = testRepository.save(test);
        finalTest = test;

        deletedQuestions.forEach(i -> questionRepository.deleteById(i));//TODO add if(exist)

        receivedQuestions.stream().forEach(q -> {
            if (createdQuestions.contains(q.getId())) {
                q.setTest(finalTest);
                questionRepository.save(q);
            } else {
                Question oldQuestion = questionRepository.getById(q.getId());
                oldQuestion.setCorrectIndexes(q.getCorrectIndexes());
                oldQuestion.setQuestionText(q.getQuestionText());
                oldQuestion.setAnswerVariants(q.getAnswerVariants());
                questionRepository.save(oldQuestion);
            }
        });

        return ResponseEntity.ok(test);
    }


    @GetMapping("/{id}")
    public FullTest getTest(@PathVariable Integer id) {
        List<Question> questions = questionRepository.findByTestIdOrderById(id);
        Test test = testRepository.getById(id);
        return new FullTest(test, questions, null, null, null);
    }


}

