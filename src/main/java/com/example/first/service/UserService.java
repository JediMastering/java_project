package com.example.first.service;

import java.util.List;

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
}
