package com.example.nlern.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "marks")
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private int id;

    @Getter @Setter
    private int mark;
    @Getter @Setter
    private int scale;



    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter @Setter
    private User user;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter @Setter
    private Test test;

    @OneToOne(cascade = CascadeType.REMOVE)
    @Getter @Setter
    private MarkDetail details;


}
