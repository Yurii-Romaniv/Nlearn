package com.example.nlern.controllers;

import com.example.nlern.models.FullTest;
import com.example.nlern.models.Question;
import com.example.nlern.models.Test;

import com.example.nlern.repos.GroupRepository;
import com.example.nlern.repos.QuestionRepository;
import com.example.nlern.repos.TestRepository;
import com.example.nlern.repos.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

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
@RequestMapping("/subload" )
public class SubloadController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("check_group/{name}" )
    public boolean checkGroupName(@PathVariable  String name){
        return groupRepository.existsByName(name);
    }

    @GetMapping("teachersHome")
    public List<Test> home(Model model){
        int teacherId = 3; //TODO get id from auth
        return  testRepository.findTop5ByAuthorIdOrderByIdDesc(teacherId);
    }


    @DeleteMapping("tests/{id}")
    public ResponseEntity deleteClient(@PathVariable Integer id) {
        questionRepository.deleteAllByTestId(id);
        testRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }


    @PostMapping("tests")
    public ResponseEntity createTest(@RequestBody FullTest fullTest) {

        int teacherId = 3; //TODO get id from auth

        Test test = fullTest.getTest();
        List<Question> questions = fullTest.getQuestions();

        test.setAuthor(userRepository.getById(teacherId));
        test.setGroup(groupRepository.getByName(fullTest.getGroupName()));
        test = testRepository.save(test);

        Test finalTest = test;
        questions.stream()
                .forEach(q-> {
                    q.setTest(finalTest);
                    questionRepository.save(q);
                });

        return ResponseEntity.ok().build();
    }

    @PutMapping("tests/{id}")
    public ResponseEntity updateTest(@PathVariable Integer id, @RequestBody FullTest fullTest) {
        Test finalTest;
        Test test = testRepository.findById(id).orElseThrow(RuntimeException::new);
        Test receivedTest = fullTest.getTest();
        List<Question> receivedQuestions = fullTest.getQuestions();

        List<Integer> createdQuestions = fullTest.getAdded();
        List<Integer> deletedQuestions = fullTest.getDeleted();

        test.setName(receivedTest.getName());
        test.setDuration(receivedTest.getDuration());
        test.setGroup(groupRepository.getByName(fullTest.getGroupName()));
        test = testRepository.save(test);
        finalTest = test;


        for (int i: deletedQuestions) { //TODO add if(exist)
            questionRepository.deleteById(i);
        }


        receivedQuestions.stream()
                .forEach(q-> {
                    if(createdQuestions.contains(q.getId())){
                        q.setTest(finalTest);
                        questionRepository.save(q);
                    }else{
                        Question oldQuestion = questionRepository.getById(q.getId());
                        oldQuestion.setAnswers(q.getAnswers());
                        oldQuestion.setQuestion(q.getQuestion());
                        oldQuestion.setAnswerVariants(q.getAnswerVariants());
                        questionRepository.save(oldQuestion);
                    }
                });


        return ResponseEntity.ok(test);
    }


    @GetMapping("tests/{id}")
    public FullTest getTest(@PathVariable Integer id){
        List<Question> questions = questionRepository.findByTestIdOrderById(id);
        Test test = testRepository.getById(id);

        FullTest fullTest = new FullTest();
        fullTest.setTest(test);
        fullTest.setQuestions(questions);

        return  fullTest;
    }


}

