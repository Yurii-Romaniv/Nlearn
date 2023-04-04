package com.example.nlern.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.example.nlern.Constants;

@Entity
@Table(name = "mark_details")
public class MarkDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private int id;


    @ElementCollection()
    @Column(length = Constants.NUMBER_OF_QUESTIONS)
    @Getter @Setter
    private List<Integer> questionId;

    @ElementCollection()
    @Column(length = 10 * Constants.NUMBER_OF_QUESTIONS)
    @Getter @Setter
    private List<Integer> answerId; //TODO work with multiple-choice questions


}
