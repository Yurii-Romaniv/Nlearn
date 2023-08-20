package com.example.nlearn.repos;


import com.example.nlearn.models.Mark;
import com.example.nlearn.models.Test;
import com.example.nlearn.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarkRepository extends CrudRepository<Mark, Integer> {

    Integer countMarksByUserAndTest(User user, Test test);

    List<Mark> getAllByUser(User user);

    List<Mark> getByTestId(int testId);

    void deleteAllByTestId(int test_id);
}
