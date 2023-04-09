package com.example.nlern.repos;

import com.example.nlern.models.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Integer> {
    List<Test> findTop5ByAuthorIdOrderByIdDesc(int Author);
    Test getById(int id);

}
