package com.example.first.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.first.entity.User;
import com.example.first.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
