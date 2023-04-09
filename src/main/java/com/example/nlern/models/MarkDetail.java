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

import com.example.nlern.Constants;

@Entity
@Table(name = "mark_details")
@Getter @Setter
public class MarkDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ElementCollection()
    @Column(length = Constants.NUMBER_OF_QUESTIONS)
    private List<Integer> questionId;

    @ElementCollection()
    @Column(length = 10 * Constants.NUMBER_OF_QUESTIONS)
    private List<Integer> answerId; //TODO work with multiple-choice questions

}
