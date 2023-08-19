package com.example.nlearn.dtos;


import com.example.nlearn.models.Group;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TestDto {
    private int id;
    private String name;
    private int duration;
    private LocalDateTime endTime;
    private int numberOfRetries;
    private int authorId;
    private Group group;
}