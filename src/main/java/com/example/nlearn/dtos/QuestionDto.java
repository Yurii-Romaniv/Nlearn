package com.example.nlearn.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionDto {
    private int id;
    private String questionText;
    private List<AnswerDto> answerVariants;
    private boolean multivariate;
}