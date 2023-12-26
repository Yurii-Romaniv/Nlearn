package com.example.nlearn.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_session_info")
@Getter
@Setter
public class TestSessionInfo {

    public void startSession(Test test, LocalDateTime endTime) {
        this.isActive = true;
        this.currentTest = test;
        this.endTime = endTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @AccessType(AccessType.Type.PROPERTY)
    private int id;

    private boolean isActive = false;

    @ManyToOne
    private Test currentTest;

    private LocalDateTime endTime;
}
