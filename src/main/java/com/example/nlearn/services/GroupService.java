package com.example.nlearn.services;

import com.example.nlearn.repos.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public boolean checkName(int id) {
        return groupRepository.getAllByTeacherId(id);
    }
