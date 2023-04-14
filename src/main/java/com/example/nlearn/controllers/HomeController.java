package com.example.nlern.controllers;

import com.example.nlern.models.Test;
import com.example.nlern.repos.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teachersHome")
public class HomeController {

    @Autowired
    private TestRepository testRepository;

    @GetMapping("/")
    public List<Test> home() {
        int teacherId = 3; //TODO get id from auth
        return testRepository.findTop5ByAuthorIdOrderByIdDesc(teacherId);
    }
}

