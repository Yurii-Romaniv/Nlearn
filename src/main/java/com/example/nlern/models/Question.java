package com.example.nlern.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private int id;


    @Getter @Setter
    private String question;

    @Getter @Setter
    private boolean isFlags;




    @ElementCollection()
    @Column(length = 1000)
    @Getter @Setter
    private List<String> answerVariants;


    @ElementCollection()
    @Column(length = 10)
    @Getter @Setter
    private List<Integer> answers;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter @Setter
    private Test test;



}
