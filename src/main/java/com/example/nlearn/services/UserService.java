package com.example.nlearn.services;

import com.example.nlearn.dtos.UserDto;
import com.example.nlearn.models.Group;
import com.example.nlearn.models.TestSessionInfo;
import com.example.nlearn.models.User;
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


    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
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

    public List<User> findAllByGroupId(Group group) {
        return userRepository.findByGroups(group);
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

    @Transactional
    public ResponseEntity createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
        user.saveGroups();

        TestSessionInfo testSessionInfo = new TestSessionInfo();
        user.setTestSessionInfo(testSessionInfo);

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity updateUser(UserDto userDto) {
        User newUser = modelMapper.map(userDto, User.class);
        User oldUser = getUser(newUser.getId());
        newUser.setTestSessionInfo(oldUser.getTestSessionInfo());

        newUser.removeGroups(
                oldUser.getGroups().stream()
                        .filter(g ->
                                newUser.getGroups()
                                        .stream()
                                        .noneMatch(gr -> gr.getId() == g.getId()))
                        .toList());

        newUser.saveGroups();
        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}

