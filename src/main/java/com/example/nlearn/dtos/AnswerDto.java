package com.example.nlearn.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {
    private int id;
    @JsonProperty(value = "isCorrect")
    private boolean isCorrect;
    private String answerText;
}
