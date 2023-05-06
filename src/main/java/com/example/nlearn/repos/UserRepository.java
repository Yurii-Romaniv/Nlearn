package com.example.nlearn.repos;

import com.example.nlearn.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findTop5ByOrderByIdDesc();

    User getById(int id);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}


