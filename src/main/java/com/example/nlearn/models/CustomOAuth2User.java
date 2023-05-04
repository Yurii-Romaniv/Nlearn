package com.example.nlearn.models;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    @Getter
    private User dbUser;

    Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    @Getter
    private boolean isAdmin;

    public CustomOAuth2User(User dbUser, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.dbUser = dbUser;
        this.authorities = authorities;
        this.attributes = attributes;
        this.isAdmin = authorities.contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return this.getAttribute("name");
    }

    public String getEmail() {
        return this.getAttribute("email");
    }
}

