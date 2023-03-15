package com.example.nlern.repos;


import com.example.nlern.models.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Integer> {

}
