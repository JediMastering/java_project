package com.example.first.service.impl;

import com.example.first.dto.CreateUserDto;
import com.example.first.dto.UserDTO;
import com.example.first.entity.Role;
import com.example.first.entity.User;
import com.example.first.exception.EntityNotFoundException;
import com.example.first.repository.RoleRepository;
import com.example.first.repository.UserRepository;
import com.example.first.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(CreateUserDto createUserDto) {
        userRepository.findByUsername(createUserDto.username())
            .ifPresent(user -> {
                throw new IllegalArgumentException("User already exists");
            });

        Role basicRole = roleRepository.findByName(Role.Values.BASIC.name())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        User user = new User();
        user.setUsername(createUserDto.username());
        user.setPassword(passwordEncoder.encode(createUserDto.password()));
        user.setRoles(Set.of(basicRole));

        User savedUser = userRepository.save(user);

        return new UserDTO(savedUser.getUserId(), savedUser.getUsername());
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new UserDTO(user.getUserId(), user.getUsername()));
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId.getMostSignificantBits(), User.class));
        return new UserDTO(user.getUserId(), user.getUsername());
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO updateUser(UUID userId, CreateUserDto createUserDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId.getMostSignificantBits(), User.class));

        user.setUsername(createUserDto.username());
        user.setPassword(passwordEncoder.encode(createUserDto.password()));

        User updatedUser = userRepository.save(user);

        return new UserDTO(updatedUser.getUserId(), updatedUser.getUsername());
    }
}