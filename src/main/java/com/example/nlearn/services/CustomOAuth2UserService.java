package com.example.nlearn.services;


import com.example.nlearn.models.CustomOAuth2User;
import com.example.nlearn.models.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        if (!userService.existsByEmail(email)) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "you are not registered", ""));
        }
        User dbUser = userService.findByEmail(email);
        List<SimpleGrantedAuthority> mappedAuthorities = List.of(new SimpleGrantedAuthority(dbUser.getRole().name()));
        return new CustomOAuth2User(dbUser, mappedAuthorities, oAuth2User.getAttributes());
    }
}