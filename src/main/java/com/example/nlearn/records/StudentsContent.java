package com.example.nlearn.records;

import com.example.nlearn.dtos.MarkDto;
import com.example.nlearn.dtos.TestDto;

import java.util.List;

public record StudentsContent(List<TestDto> tests, List<MarkDto> marks) {
}