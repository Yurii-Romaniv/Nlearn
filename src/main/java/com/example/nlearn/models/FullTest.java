package com.example.nlern.models;

import java.util.List;

public record FullTest(Test test, List<Question> questions, List<Integer> deletedIds, List<Integer> addedIds, String groupName) {
}
