package com.example.nlearn.controllers;

import com.example.nlearn.models.FullTest;

import com.example.nlearn.models.Test;
import com.example.nlearn.models.CustomOAuth2User;
import com.example.nlearn.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("teachersHome/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/first5")
    public List<Test> home(@AuthenticationPrincipal CustomOAuth2User user) {
        return testService.getTop5Tests(user.getDbUser().getId());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Integer id, @AuthenticationPrincipal CustomOAuth2User user) {
        return testService.deleteTest(id, user.getDbUser().getId(), user.isAdmin());
    }

    @PostMapping("/new")
    public ResponseEntity createTest(@RequestBody FullTest fullTest, @AuthenticationPrincipal CustomOAuth2User user) {
        return testService.createTest(fullTest, user.getDbUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTest(@PathVariable Integer id, @RequestBody FullTest fullTest, @AuthenticationPrincipal CustomOAuth2User user) {
        return testService.updateTest(id, fullTest, user.getDbUser().getId(), user.isAdmin());
    }

    @GetMapping("/{id}")
    public FullTest getTest(@PathVariable Integer id, @AuthenticationPrincipal CustomOAuth2User user) {
        return testService.getTest(id, user.getDbUser(), user.isAdmin());
    }
}

