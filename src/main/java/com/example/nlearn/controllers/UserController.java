package com.example.nlearn.controllers;

import com.example.nlearn.dtos.UserDto;
import com.example.nlearn.models.User;
import com.example.nlearn.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admins-home/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/load")
    public List<User> getUsers() {
        return userService.getTop5Users();
    }

    @GetMapping("/load-all")
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/new")
    public ResponseEntity createUser(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @PutMapping("/")
    public ResponseEntity updateUsers(@RequestBody UserDto user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUserDto(id);
    }
}