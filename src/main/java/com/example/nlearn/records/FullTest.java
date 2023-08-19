package com.example.nlearn.records;

import com.example.nlearn.models.Group;
import com.example.nlearn.models.Question;
import com.example.nlearn.dtos.TestDto;

import java.util.List;

public record FullTest(TestDto test, List<Question> questions, List<Group> groups, List<Integer> deletedIds, List<Integer> addedIds) {
}