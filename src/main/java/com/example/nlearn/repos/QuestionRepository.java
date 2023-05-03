package com.example.nlearn.repos;


import com.example.nlearn.models.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
    List<Question> findByTestIdOrderById(int TestId);

    @Transactional
    void deleteAllByTestId(int TestId);

    Question getById(int id);
}
