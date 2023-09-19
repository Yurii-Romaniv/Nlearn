package com.example.nlearn.services;

import com.example.nlearn.dtos.UserDto;
import com.example.nlearn.models.TestSessionInfo;
import com.example.nlearn.models.User;
import com.example.nlearn.repos.TestSessionInfoRepository;
import com.example.nlearn.repos.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Service
@CrossOrigin
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TestSessionInfoRepository testSessionInfoRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, TestSessionInfoRepository testSessionInfoRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.testSessionInfoRepository = testSessionInfoRepository;
    }

    public User getUser(Integer id) {
        return userRepository.getById(id);
    }

    @Transactional
    public UserDto getUserDto(Integer id) {
        User user = userRepository.getById(id);
        return modelMapper.map(user, UserDto.class);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAllByGroupId(int id) {
        return userRepository.findByGroupIdOrderByName(id);
    }

    public List<User> getTop5Users() {
        return userRepository.findTop5ByOrderByIdDesc();
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByIdDesc();
    }

    public ResponseEntity<Object> deleteUser(Integer id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        TestSessionInfo testSessionInfo = new TestSessionInfo();
        testSessionInfoRepository.save(testSessionInfo);
        user.setTestSessionInfo(testSessionInfo);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity updateUser(UserDto userDto) {
        User newUser = modelMapper.map(userDto, User.class);
        User oldUser = getUser(newUser.getId());
        newUser.setTestSessionInfo(oldUser.getTestSessionInfo());
        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}

