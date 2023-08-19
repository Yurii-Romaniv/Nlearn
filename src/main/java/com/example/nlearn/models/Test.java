package com.example.nlearn.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tests")
@Getter
@Setter
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @AccessType(AccessType.Type.PROPERTY)
    private int id;

    private String name;
    private int duration;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne
    private Group group;  //TODO rewrite in ManyToMany

    private LocalDateTime endTime;

    private int numberOfRetries;
}
