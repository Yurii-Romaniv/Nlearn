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
    private QuestionRepository questRepo;

    @Autowired
    private TestRepository testRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository usRepo;


    @GetMapping("check_group/{name}" )
    public boolean checkGroupName(@PathVariable  String name){
        return groupRepo.existsByName(name);
    }

    @GetMapping("teachersHome")
    public List<Test> home(Model model){
        int teacherId = 3; //TODO get id from auth
        return  testRepo.findTop5ByAuthorIdOrderByIdDesc(teacherId);
    }


    @DeleteMapping("tests/{id}")
    public ResponseEntity deleteClient(@PathVariable Integer id) {
        questRepo.deleteAllByTestId(id);
        testRepo.deleteById(id);

        return ResponseEntity.ok().build();
    }


    @PostMapping("tests")
    public ResponseEntity createTest(@RequestBody FullTest fullTest) {

        int teacherId = 3; //TODO get id from auth

        Test test = fullTest.getTest();
        List<Question> questions = fullTest.getQuestions();

        test.setAuthor(usRepo.getById(teacherId));
        test.setGroup(groupRepo.getByName(fullTest.getGroupName()));
        test = testRepo.save(test);

        Test finalTest = test;
        questions.stream()
                .forEach(q-> {
                    q.setTest(finalTest);
                    questRepo.save(q);
                });

        return ResponseEntity.ok().build();
    }

    @PutMapping("tests/{id}")
    public ResponseEntity updateTest(@PathVariable Integer id, @RequestBody FullTest fullTest) {
        Test finalTest;
        Test test = testRepo.findById(id).orElseThrow(RuntimeException::new);
        Test receivedTest = fullTest.getTest();
        List<Question> receivedQuestions = fullTest.getQuestions();

        List<Integer> createdQuestions = fullTest.getAdded();
        List<Integer> deletedQuestions = fullTest.getDeleted();

        test.setName(receivedTest.getName());
        test.setDuration(receivedTest.getDuration());
        test.setGroup(groupRepo.getByName(fullTest.getGroupName()));
        test = testRepo.save(test);
        finalTest = test;


        for (int i: deletedQuestions) { //TODO add if(exist)
            questRepo.deleteById(i);
        }


        receivedQuestions.stream()
                .forEach(q-> {
                    if(createdQuestions.contains(q.getId())){
                        q.setTest(finalTest);
                        questRepo.save(q);
                    }else{
                        Question oldQuestion = questRepo.getById(q.getId());
                        oldQuestion.setAnswers(q.getAnswers());
                        oldQuestion.setQuestion(q.getQuestion());
                        oldQuestion.setAnswerVariants(q.getAnswerVariants());
                        questRepo.save(oldQuestion);
                    }
                });


        return ResponseEntity.ok(test);
    }


    @GetMapping("tests/{id}")
    public FullTest getTest(@PathVariable Integer id){
        List<Question> questions = questRepo.findByTestIdOrderById(id);
        Test test = testRepo.getById(id);

        FullTest fullTest = new FullTest();
        fullTest.setTest(test);
        fullTest.setQuestions(questions);

        return  fullTest;
    }


}

