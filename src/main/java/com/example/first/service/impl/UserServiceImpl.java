package com.example.first.service.impl;

import com.example.first.dto.CreateUserDto;
import com.example.first.dto.UserDTO;
import com.example.first.dto.UserFilter;
import com.example.first.entity.AccessGroup;
import com.example.first.entity.User;
import com.example.first.exception.EntityNotFoundException;
import com.example.first.repository.AccessGroupRepository;
import com.example.first.repository.UserRepository;
import com.example.first.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccessGroupRepository accessGroupRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, AccessGroupRepository accessGroupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accessGroupRepository = accessGroupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(CreateUserDto createUserDto) {
        userRepository.findByUsername(createUserDto.username())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("User already exists");
                });

        User user = new User();
        user.setUsername(createUserDto.username());
        user.setPassword(passwordEncoder.encode(createUserDto.password()));

        if (createUserDto.accessGroupIds() != null && !createUserDto.accessGroupIds().isEmpty()) {
            java.util.Set<AccessGroup> accessGroups = new HashSet<>(accessGroupRepository.findAllById(createUserDto.accessGroupIds()));
            user.setAccessGroups(accessGroups);
        }

        User savedUser = userRepository.save(user);

        return new UserDTO(savedUser.getUserId(), savedUser.getUsername());
    }

    @Override
    public Page<UserDTO> getAllUsers(UserFilter userFilter, Pageable pageable) {
        // TODO: Implementar filtro por permissões se necessário, por enquanto apenas busca todos
        return userRepository.findAll(userFilter, pageable)
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

        if (createUserDto.accessGroupIds() != null && !createUserDto.accessGroupIds().isEmpty()) {
            java.util.Set<AccessGroup> accessGroups = new HashSet<>(accessGroupRepository.findAllById(createUserDto.accessGroupIds()));
            user.setAccessGroups(accessGroups);
        } else {
            user.setAccessGroups(new HashSet<>());
        }

        User updatedUser = userRepository.save(user);

        return new UserDTO(updatedUser.getUserId(), updatedUser.getUsername());
    }
}
