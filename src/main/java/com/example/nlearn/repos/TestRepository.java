package com.example.nlearn.repos;

import com.example.nlearn.models.Test;
import com.example.nlearn.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Integer> {
    List<Test> findTop5ByAuthorIdOrderByIdDesc(int authorId);

    List<Test> findTop5ByGroupIdOrderByIdDesc(int id);

    Test getById(int id);

    Test getTestById(int id);

}
