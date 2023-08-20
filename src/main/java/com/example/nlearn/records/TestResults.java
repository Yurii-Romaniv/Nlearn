package com.example.nlearn.records;

import com.example.nlearn.dtos.MarkDto;
import com.example.nlearn.models.User;

import java.util.List;

public record TestResults(List<User> users, List<MarkDto> marks) {
}