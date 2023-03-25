package com.example.nlern.controllers;

import com.example.nlern.models.*;
import com.example.nlern.repos.GroupRepository;
import com.example.nlern.repos.QuestionRepository;
import com.example.nlern.repos.TestRepository;
import com.example.nlern.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/subload")
public class SubloadController {

    @Autowired
    private QuestionRepository QuestRepo;

    @Autowired
    private TestRepository TestRepo;

    @Autowired
    private GroupRepository GroupRepo;

    @Autowired
    private UserRepository UsRepo;


    @PostMapping("/check_group")
    public Map<String, Object> createUser(@RequestParam("group") String group){

        Map<String, Object> map = new HashMap<>();
        map.put("groupExist", GroupRepo.existsByName(group));
        return map;
    }

    @GetMapping("teachersHome")
    public List<Test> home(Model model){
        int teacherId = 3; //------------- temp imitation until auth not added
        //List<User> users= UsRepo.findTop5ByOrderByIdDesc();
        return  TestRepo.findTop5ByAuthorIdOrderByIdDesc(teacherId);
    }


    @DeleteMapping("tests/{id}")
    public ResponseEntity deleteClient(@PathVariable Integer id) {

        /*
        for (Question q: QuestRepo.findByTestIdOrderById(id)) {
            // need add if(exist)
            QuestRepo.deleteById(q.getId());
            continue;
        }

         */

        QuestRepo.deleteAllByTestId(id);



        TestRepo.deleteById(id);





        return ResponseEntity.ok().build();
    }


    @PostMapping("tests")
    public ResponseEntity createClient( @RequestBody FullTest fullTest) {//throws URISyntaxException {
        //Client savedClient = clientRepository.save(client);
        //return ResponseEntity.created(new URI("/clients/" + savedClient.getId())).body(savedClient);

        int teacherId = 3; //------------- temp imitation until auth not added

        Test test=  (fullTest.getTest()).get();
        List<Question> questions=  (fullTest.getQuestions());

        test.setAuthor(UsRepo.findById(teacherId).get());
        test = TestRepo.save(test);


        for (Question q: questions) {
            q.setTest(test);
            QuestRepo.save(q);

        }


        return ResponseEntity.ok(test);
    }

    @PutMapping("tests/{id}")
    public ResponseEntity updateTest(@PathVariable Integer id, @RequestBody FullTest fullTest) {

        Test test = TestRepo.findById(id).orElseThrow(RuntimeException::new);
        Test receivedTest=  (fullTest.getTest()).get();
        List<Question> receivedQuestions=  (fullTest.getQuestions());

        List<Integer> createdQuestions =fullTest.getAdded();
        List<Integer> deletedQuestions =fullTest.getDeleted();



        test.setName(receivedTest.getName());
        test.setDuration(receivedTest.getDuration());
        test = TestRepo.save(test);



        for (int i: deletedQuestions) {
            // need add if(exist)
            QuestRepo.deleteById(i);
            continue;
        }


        for (Question q: receivedQuestions) {

            if(  createdQuestions.contains(q.getId()) ){
                q.setTest(test);
                QuestRepo.save(q);
                continue;
            }


            Question oldQuestion= QuestRepo.findById(q.getId()).get();
            //oldQuestion.setAnswers(q.getAnswers());
            //oldQuestion.setAnswerVariants(q.getAnswerVariants());
            QuestRepo.save(q);
        }


        return ResponseEntity.ok(test);
    }


    @GetMapping("tests/{id}")
    public FullTest getTest(Model model, @PathVariable Integer id){
        int teacherId = 3; //------------- temp imitation until auth not added
        //List<User> users= UsRepo.findTop5ByOrderByIdDesc();
        List<Question> questions=QuestRepo.findByTestIdOrderById(id);
        Optional<Test> test =TestRepo.findById(id);

        FullTest fullTest = new FullTest();
        fullTest.setTest(test);
        fullTest.setQuestions(questions);
        //Map<String, Object> map = new HashMap<>();
        //map.put("test", test);
        //map.put("questions", questions);

        return  fullTest;
    }


    @GetMapping("/t")
    public List<Group> homePage(){
        return (List<Group>) GroupRepo.findAll();
    }
}

