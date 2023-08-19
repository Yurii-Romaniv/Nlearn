package com.example.nlearn.services;

import com.example.nlearn.models.Mark;
import com.example.nlearn.models.Test;
import com.example.nlearn.models.User;
import com.example.nlearn.repos.MarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Service
@CrossOrigin
public class MarkService {
    private final MarkRepository markRepository;

    public MarkService(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    public List<Mark> getAllByUser(User user) {
        return markRepository.getAllByUser(user);
    }

    public void save(Mark mark) { markRepository.save(mark); }

    int countMarksByUserAndTest(User user, Test test) { return markRepository.countMarksByUserAndTest(user, test); }
}