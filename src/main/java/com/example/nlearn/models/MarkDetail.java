package com.example.nlern.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "mark_details")
@Getter
@Setter
public class MarkDetail {
    private static final int NUMBER_OF_QUESTIONS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ElementCollection()
    @Column(length = NUMBER_OF_QUESTIONS)
    private List<Integer> questionIds;

    @ElementCollection()
    @Column(length = 10 * NUMBER_OF_QUESTIONS)
    private List<Integer> answerIds; //TODO work with multiple-choice questions

}
