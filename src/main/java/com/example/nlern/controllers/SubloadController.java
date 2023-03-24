package com.example.nlern.controllers;

import com.example.nlern.models.User;
import com.example.nlern.repos.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/subload")
public class subloadController {
    @Autowired
    private GroupRepository GroupRepo;
    @PostMapping("/check_group")
    public Map<String, Object> createUser(@RequestParam("group") String group){

        Map<String, Object> map = new HashMap<>();
        map.put("groupExist", GroupRepo.existsByName(group));
        return map;
    }
}
