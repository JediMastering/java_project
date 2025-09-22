package com.example.first.service;

import com.example.first.dto.CreateUserDto;
import com.example.first.dto.UpdateUserDto;
import com.example.first.dto.UserDTO;
import com.example.first.dto.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDTO createUser(CreateUserDto createUserDto);

    Page<UserDTO> getAllUsers(UserFilter userFilter, Pageable pageable);

    UserDTO getUserById(Long userId);

    void deleteUser(Long userId);

    UserDTO updateUser(Long userId, UpdateUserDto updateUserDto);
}