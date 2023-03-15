package com.example.nlern.models;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "mark details")
public class MarkDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;




    @ElementCollection()
    @Column(length = 10)
    private List<Integer> guestionId;

    @ElementCollection()
    @Column(length = 10)
    private List<Integer> answerId; //-----!!!!!!!!!!!!!!!!!!! need rewrite to work with multiple-choice questions




    public List<Integer> getGuestionId() {
        return guestionId;
    }

    public void setGuestionId(List<Integer> guestionId) {
        this.guestionId = guestionId;
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
