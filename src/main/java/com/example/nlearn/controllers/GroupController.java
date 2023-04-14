package com.example.nlern.controllers;

import com.example.nlern.repos.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;


    @GetMapping("/check/{name}")
    public boolean checkGroupName(@PathVariable String name) {
        return groupRepository.existsByName(name);
    }


}

