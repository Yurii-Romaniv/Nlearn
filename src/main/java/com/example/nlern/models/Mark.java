package com.example.nlern.models;


import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "marks")
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    private int mark;

    private int scale;



    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Test test;

    @OneToOne(cascade = CascadeType.REMOVE)
    private MarkDetail details;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public MarkDetail getDetails() {
        return details;
    }

    public void setDetails(MarkDetail details) {
        this.details = details;
    }
}
