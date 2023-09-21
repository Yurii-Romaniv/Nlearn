package com.example.nlearn.records;

import com.example.nlearn.dtos.MarkDto;
import com.example.nlearn.dtos.UserDto;

import java.util.List;

public record TestResults(List<UserDto> users, List<MarkDto> marks) {
}