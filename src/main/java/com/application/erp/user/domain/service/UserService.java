package com.application.erp.user.domain.service;

import com.application.erp.user.dto.request.CreateUserDto;
import com.application.erp.user.dto.request.UpdateUserDto;
import com.application.erp.user.dto.response.UserDTO;
import com.application.erp.user.dto.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDTO createUser(CreateUserDto createUserDto);

    Page<UserDTO> getAllUsers(UserFilter userFilter, Pageable pageable);

    UserDTO getUserById(Long userId);

    void deleteUser(Long userId);

    UserDTO updateUser(Long userId, UpdateUserDto updateUserDto);
}