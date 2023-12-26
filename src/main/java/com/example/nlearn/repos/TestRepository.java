package com.example.nlearn.repos;

import com.example.nlearn.models.Group;
import com.example.nlearn.models.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TestRepository extends CrudRepository<Test, Integer> {
    List<Test> findTop5ByAuthorIdOrderByIdDesc(int authorId);

    Test getById(int id);

    Test getTestById(int id);

    List<Test> findAllByAuthorIdOrderByIdDesc(int authorId);

    List<Test> findByGroupInOrderByIdDesc(Set<Group> groups);
}