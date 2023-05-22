package com.example.nlearn.services;

import com.example.nlearn.models.Question;
import com.example.nlearn.repos.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Service
@CrossOrigin
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    public void deleteAllByTestId(Integer id) {
        questionRepository.deleteAllByTestId(id);
    }

    public void deleteById(Integer id) {
        questionRepository.deleteById(id);
    }

    public void save(Question question) {
        questionRepository.save(question);
    }

    public Question getById(Integer id) {
        return questionRepository.getById(id);
    }

    public List<Question> findByTestId(Integer id) {
        return questionRepository.findByTestIdOrderById(id);
    }

    public List<Question> findByTestIdWithoutAnswers(Integer id) {
        List<Question> questions = questionRepository.getByTestIdOrderById(id);
        questions.forEach(q -> q.setCorrectIndexes(null));
        return questions;
    }
}

