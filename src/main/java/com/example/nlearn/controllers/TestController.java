package com.example.nlearn.controllers;

import com.example.nlearn.dtos.TestDto;
import com.example.nlearn.records.FullTest;

import com.example.nlearn.models.CustomOAuth2User;
import com.example.nlearn.records.TestResults;
import com.example.nlearn.services.TestService;
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
@RequestMapping("teachers-home/tests")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/load")
    public List<TestDto> getTests(@AuthenticationPrincipal CustomOAuth2User user) {
        return testService.getTop5Tests(user.getDbUser().getId());
    }

    @GetMapping("/load-all")
    public List<TestDto> getAllTests(@AuthenticationPrincipal CustomOAuth2User user) {
        return testService.getAllTests(user.getDbUser().getId());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteTest(@PathVariable Integer id, @AuthenticationPrincipal CustomOAuth2User user) {
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

    @GetMapping("/{id}/results")
    public TestResults getTestResults(@PathVariable Integer id, @AuthenticationPrincipal CustomOAuth2User user) {
        return testService.getTestResults(id, user.getDbUser(), user.isAdmin());
    }
}

