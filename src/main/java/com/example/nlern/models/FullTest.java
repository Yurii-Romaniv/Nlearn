package com.example.nlern.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FullTest {
    Optional<Test>  test;
    List<Question> questions;

    List<Integer> deleted;
    List<Integer> added ;

    public Optional<Test>  getTest() {
        return test;
    }

    public void setTest(Optional<Test> test) {
        this.test = test;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    public List<Integer> getDeleted() {
        return deleted;
    }

    public void setDeleted(ArrayList<Integer> deleted) {
        this.deleted = deleted;
    }

    public List<Integer> getAdded() {
        return added;
    }

    public void setAdded(ArrayList<Integer> added) {
        this.added = added;
    }
}
