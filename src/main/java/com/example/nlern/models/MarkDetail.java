package com.example.nlern.models;
import jakarta.persistence.*;

import java.util.List;

import static com.example.nlern.Constants.NUMBER_OF_QUESTIONS;

@Entity
@Table(name = "mark details")
public class MarkDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @ElementCollection()
    @Column(length = NUMBER_OF_QUESTIONS)
    private List<Integer> questionId;

    @ElementCollection()
    @Column(length = 10 * NUMBER_OF_QUESTIONS)
    private List<Integer> answerId; //-----!!!!!!!!!!!!!!!!!!! need rewrite to work with multiple-choice questions


    public List<Integer> getQuestionId() {
        return questionId;
    }

    public void setQuestionId(List<Integer> questionId) {
        this.questionId = questionId;
    }

    public List<Integer> getAnswerId() {
        return answerId;
    }

    public void setAnswerId(List<Integer> answerId) {
        this.answerId = answerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
