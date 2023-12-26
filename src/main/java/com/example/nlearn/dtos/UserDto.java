package com.example.nlearn.dtos;

import com.example.nlearn.models.Group;
import com.example.nlearn.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class UserDto {
    private int id;
    private String name;
    private String email;
    private Role role;
    private Set<Group> groups;
}
