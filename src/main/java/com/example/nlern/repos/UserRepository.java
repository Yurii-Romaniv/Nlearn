package com.example.nlern.repos;

import com.example.nlern.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository  extends CrudRepository<User, Integer> {
    List<User> findTop5ByOrderByIdDesc();

    User getById(int id);

}


