package com.example.nlearn.services;

import com.example.nlearn.models.Group;
import com.example.nlearn.repos.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getGroups() {
        return groupRepository.findAll();
    }
}