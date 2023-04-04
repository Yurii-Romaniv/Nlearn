package com.example.nlern.repos;


import com.example.nlern.models.Question;
import com.example.nlern.models.Test;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
    List<Question> findByTestIdOrderById(int Test);

    @Transactional
    void deleteAllByTestId(int Test);

    Question getById(int id);
}
