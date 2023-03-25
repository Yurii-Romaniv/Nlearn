package com.example.nlern.models;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    private String question;

    private boolean isFlags;




    @ElementCollection()
    @Column(length = 1000)
    private List<String> answerVariants;


    @ElementCollection()
    @Column(length = 10)
    private List<Integer> answers;

    @ManyToOne
    private Test test;






    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isFlags() {
        return isFlags;
    }

    public void setFlags(boolean flags) {
        isFlags = flags;
    }

    public List<String> getAnswerVariants() {
        return answerVariants;
    }

    public void setAnswerVariants(List<String> answerVariants) {
        this.answerVariants = answerVariants;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
