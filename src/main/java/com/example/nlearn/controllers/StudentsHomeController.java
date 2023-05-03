package com.example.nlearn.controllers;

import com.example.nlearn.models.CustomOAuth2User;
import com.example.nlearn.models.Test;
import com.example.nlearn.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("studentsHome")
public class StudentsHomeController {

    @Autowired
    private TestService testService;

    @GetMapping("/first5")
    public List<Test> home(@AuthenticationPrincipal CustomOAuth2User user) {
        return testService.getTop5TestsForStudent(user.getDbUser().getId());
    }
}
