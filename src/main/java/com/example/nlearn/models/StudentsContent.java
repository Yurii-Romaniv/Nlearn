package com.example.nlearn.models;

import java.util.List;

public record StudentsContent(List<Test> tests, List<Mark> marks) {
}
// it's called that because I'm going to add more objects here