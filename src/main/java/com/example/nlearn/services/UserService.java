package com.example.nlearn.services;

import com.example.nlearn.models.User;
import com.example.nlearn.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Service
@CrossOrigin
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

