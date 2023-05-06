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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public int getIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user.getId();
    }

    public int getGroupIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user.getGroup().getId();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


}

