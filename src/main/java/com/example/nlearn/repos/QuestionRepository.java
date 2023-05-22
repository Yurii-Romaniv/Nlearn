package com.example.nlearn.repos;


import com.example.nlearn.models.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
    List<Question> findByTestIdOrderById(int TestId);

    List<Question> getByTestIdOrderById(int TestId);

    @Transactional
    void deleteAllByTestId(int TestId);

    Question getById(int id);
}
