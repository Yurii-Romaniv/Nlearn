package com.example.nlern.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String questionText;
    private boolean isFlags;

    @ElementCollection()
    @Column(length = 1000)
    private List<String> answerVariants;

    @ElementCollection()
    @Column(length = 10)
    private List<Integer> correctIndexes;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Test test;
}
