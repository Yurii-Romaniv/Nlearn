package com.example.nlern.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;

    @Getter @Setter
    short duration;


   @ManyToOne
   @Getter @Setter
    private User author;

    @ManyToOne
    @Getter @Setter
    private Group group;  // good place to use ManyToMany


}
