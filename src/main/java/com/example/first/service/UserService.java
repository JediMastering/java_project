package com.example.first.service;

import com.example.first.dto.CreateUserDto;
import com.example.first.dto.UserDTO;
import com.example.first.dto.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserDTO createUser(CreateUserDto createUserDto);

    Page<UserDTO> getAllUsers(UserFilter userFilter, Pageable pageable);

    UserDTO getUserById(UUID userId);

    void deleteUser(UUID userId);

    UserDTO updateUser(UUID userId, CreateUserDto createUserDto);
}
