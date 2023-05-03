package com.example.nlearn.repos;

import com.example.nlearn.models.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Integer> {
    List<Test> findTop5ByAuthorIdOrderByIdDesc(int AuthorId);

    Test getById(int id);

}
