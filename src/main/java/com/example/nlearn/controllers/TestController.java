package com.example.nlearn.controllers;

import com.example.nlearn.models.FullTest;

import com.example.nlearn.models.Test;
import com.example.nlearn.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/first5")
    public List<Test> home() {
        int teacherId = 3; //TODO get id from auth
        return testService.getTop5Tests(teacherId);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Integer id) {
        return testService.deleteTest(id);
    }

    @PostMapping("/new")
    public ResponseEntity createTest(@RequestBody FullTest fullTest) {
        return testService.createTest(fullTest);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTest(@PathVariable Integer id, @RequestBody FullTest fullTest) {
        return testService.updateTest(id, fullTest);
    }

    @GetMapping("/{id}")
    public FullTest getTest(@PathVariable Integer id) {
        return testService.getTest(id);
    }
}

