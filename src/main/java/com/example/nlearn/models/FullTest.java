package com.example.nlearn.models;

import java.util.List;

public record FullTest(Test test, List<Question> questions, List<Group> groups, List<Integer> deletedIds, List<Integer> addedIds) {
}
