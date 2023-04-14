package com.example.nlern.repos;


import com.example.nlern.models.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
    List<Question> findByTestIdOrderById(int TestId);

    @Transactional
    void deleteAllByTestId(int TestId);

    Question getById(int id);
}
