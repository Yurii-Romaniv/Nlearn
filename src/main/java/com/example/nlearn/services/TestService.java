package com.example.nlearn.services;

import com.example.nlearn.models.FullTest;
import com.example.nlearn.models.Group;
import com.example.nlearn.models.Question;
import com.example.nlearn.models.Test;
import com.example.nlearn.repos.GroupRepository;
import com.example.nlearn.repos.QuestionRepository;
import com.example.nlearn.repos.TestRepository;
import com.example.nlearn.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Service
@CrossOrigin
public class TestService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity deleteTest(Integer id) {
        questionRepository.deleteAllByTestId(id);
        testRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }


    public ResponseEntity createTest(FullTest fullTest) {

        int teacherId = 3; //TODO get id from auth

        Test test = fullTest.test();
        List<Question> questions = fullTest.questions();

        test.setAuthor(userRepository.getById(teacherId));
        test = testRepository.save(test);

        Test finalTest = test;
        questions.stream().forEach(q -> {
            q.setTest(finalTest);
            questionRepository.save(q);
        });

        return ResponseEntity.ok().build();
    }


    public ResponseEntity updateTest(Integer id, FullTest fullTest) {
        Test finalTest;
        Test test = testRepository.findById(id).orElseThrow(RuntimeException::new);
        Test receivedTest = fullTest.test();
        List<Question> receivedQuestions = fullTest.questions();

        List<Integer> createdQuestions = fullTest.addedIds();
        List<Integer> deletedQuestions = fullTest.deletedIds();

        test.setName(receivedTest.getName());
        test.setDuration(receivedTest.getDuration());
        test.setGroup(receivedTest.getGroup());
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


    public FullTest getTest(Integer id) {
        List<Question> questions = questionRepository.findByTestIdOrderById(id);
        Test test = testRepository.getById(id);
        List<Group>groups= groupRepository.findAll();
        return new FullTest(test, questions, groups, null, null);
    }

    public List<Test> getTop5Tests(int teacherId) {
        return testRepository.findTop5ByAuthorIdOrderByIdDesc(teacherId);
    }

}

