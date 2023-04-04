package com.example.nlern.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FullTest {
    @Getter @Setter
    Test  test;
    @Getter @Setter
    List<Question> questions;

    @Getter @Setter
    List<Integer> deleted;
    @Getter @Setter
    List<Integer> added ;
    @Getter @Setter
    String groupName;


}
