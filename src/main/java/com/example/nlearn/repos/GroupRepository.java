package com.example.nlearn.repos;

import com.example.nlearn.models.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Integer> {

    List<Group> findAll();

    Group getByName(String name);

}
