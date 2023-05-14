package com.example.nlearn.controllers;

import com.example.nlearn.models.CustomOAuth2User;
import com.example.nlearn.models.FullTest;
import com.example.nlearn.models.Question;
import com.example.nlearn.models.StudentsContent;
import com.example.nlearn.services.TestPassingService;
import com.example.nlearn.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("students-home")
public class StudentsHomeController {

    @Autowired
    private TestService testService;

    @Autowired
    private TestPassingService testPassingService;


    @GetMapping("/load-tests")
    public StudentsContent home(@AuthenticationPrincipal CustomOAuth2User user) {
        return testService.getContentForStudent(user.getDbUser());
    }

    @GetMapping("/start-test/{id}")
    public FullTest testing(@PathVariable Integer id, @AuthenticationPrincipal CustomOAuth2User user) {
        return testPassingService.startTest(id, user.getDbUser());
    }

    @PostMapping("/end-test/{id}")
    public ResponseEntity testCheck(@RequestBody List<Question> questions, @PathVariable Integer id, @AuthenticationPrincipal CustomOAuth2User user) {
        return testPassingService.endTest(id, user.getDbUser(), questions);
    }
}
