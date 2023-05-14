package com.example.nlearn.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_session_info")
@Getter
@Setter
public class TestSessionInfo {

    public void startSession(int testId, LocalDateTime endTime) {
        this.isActive = true;
        this.currentTestId = testId;
        this.endTime = endTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private boolean isActive = false;
    private int currentTestId;

    private LocalDateTime endTime;
}
