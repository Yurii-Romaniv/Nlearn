package com.example.nlearn.records;

import com.example.nlearn.dtos.QuestionForStudentDto;
import com.example.nlearn.dtos.TestDto;

import java.util.List;

public record FullTestForPassing(TestDto test, List<QuestionForStudentDto> questions) {
}