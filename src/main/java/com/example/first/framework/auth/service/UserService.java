package com.example.first.framework.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.first.framework.auth.entity.User;
import com.example.first.framework.auth.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
