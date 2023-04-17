package com.example.nlearn.services;

import com.example.nlearn.models.User;
import com.example.nlearn.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@CrossOrigin
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User getById(Integer id) {
        return userRepository.getById(id);
    }
}

