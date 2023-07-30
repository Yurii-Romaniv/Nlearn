package com.example.nlearn.controllers;

import com.example.nlearn.models.CustomOAuth2User;
import com.example.nlearn.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/whoami")
public class AuthUserController {

    @GetMapping()
    public User getGroups(@AuthenticationPrincipal CustomOAuth2User user) {
        return user.getDbUser();
    }
}