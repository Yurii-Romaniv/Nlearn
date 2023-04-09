package com.example.nlern.models;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class FullTest {
    Test test;
    List<Question> questions;
    List<Integer> deleted;
    List<Integer> added ;
    String groupName;
}
