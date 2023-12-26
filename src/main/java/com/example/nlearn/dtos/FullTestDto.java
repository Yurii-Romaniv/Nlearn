package com.example.nlearn.dtos;


import com.example.nlearn.models.Group;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FullTestDto {
    private int id;
    private String name;
    private int duration;
    private LocalDateTime endTime;
    private int numberOfRetries;
    private List<QuestionDto> questions;
    private Group group;
}