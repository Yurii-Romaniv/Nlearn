package com.example.nlearn.controllers;

import com.example.nlearn.models.Group;
import com.example.nlearn.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teachersHome/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping()
    public List<Group> getGroups() {
        return groupService.getGroups();
    }
}