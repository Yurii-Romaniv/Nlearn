package com.example.nlearn.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionForStudentDto {
    private int id;
    private String questionText;
    private int numberOfCorrectAnswers = 1;
    private List<String> answerVariants;
}