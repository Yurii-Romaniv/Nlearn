package com.example.nlearn.controllers;

import com.example.nlearn.models.FullTest;

import com.example.nlearn.models.Test;
import com.example.nlearn.models.User;
import com.example.nlearn.services.TestService;
import com.example.nlearn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
@RequestMapping("studentsHome")
public class StudentsHomeController {

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;

    @GetMapping("/first5")
    public List<Test> home(@AuthenticationPrincipal OAuth2User user) {
        int studentId = userService.getGroupIdByEmail(user.getAttribute("email")); //temp
        return testService.getTop5TestsForStudent(studentId);
    }


}
