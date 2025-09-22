package com.example.first.service.impl;

import com.example.first.config.AttachmentType;
import com.example.first.dto.CreateUserDto;
import com.example.first.dto.UpdateUserDto;
import com.example.first.dto.UserDTO;
import com.example.first.dto.UserFilter;
import com.example.first.entity.AccessGroup;
import com.example.first.entity.Attachment;
import com.example.first.entity.User;
import com.example.first.exception.EntityNotFoundException;
import com.example.first.exception.UserAlreadyExistsException;
import com.example.first.repository.AccessGroupRepository;
import com.example.first.repository.UserRepository;
import com.example.first.service.AttachmentService;
import com.example.first.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccessGroupRepository accessGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttachmentService attachmentService;

    public UserServiceImpl(UserRepository userRepository, AccessGroupRepository accessGroupRepository, PasswordEncoder passwordEncoder, AttachmentService attachmentService) {
        this.userRepository = userRepository;
        this.accessGroupRepository = accessGroupRepository;
        this.passwordEncoder = passwordEncoder;
        this.attachmentService = attachmentService;
    }

    @Override
    public UserDTO createUser(CreateUserDto createUserDto) {
        userRepository.findByUsername(createUserDto.username())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User already exists");
                });

        User user = new User();
        user.setUsername(createUserDto.username());
        user.setPassword(passwordEncoder.encode(createUserDto.password()));

        if (createUserDto.accessGroupIds() != null && !createUserDto.accessGroupIds().isEmpty()) {
            java.util.Set<AccessGroup> accessGroups = new HashSet<>(accessGroupRepository.findAllById(createUserDto.accessGroupIds()));
            user.setAccessGroups(accessGroups);
        }

        User savedUser = userRepository.save(user);

        return toUserDTO(savedUser);
    }

    @Override
    public Page<UserDTO> getAllUsers(UserFilter userFilter, Pageable pageable) {
        return userRepository.findAll(userFilter, pageable)
                .map(this::toUserDTO);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId, User.class));
        return toUserDTO(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO updateUser(Long userId, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId, User.class));

        if (updateUserDto.username() != null && !updateUserDto.username().isBlank()) {
            user.setUsername(updateUserDto.username());
        }

        if (updateUserDto.password() != null && !updateUserDto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateUserDto.password()));
        }

        if (updateUserDto.accessGroupIds() != null) {
            java.util.Set<AccessGroup> accessGroups = new HashSet<>(accessGroupRepository.findAllById(updateUserDto.accessGroupIds()));
            user.setAccessGroups(accessGroups);
        }

        User updatedUser = userRepository.save(user);

        return toUserDTO(updatedUser);
    }

    private UserDTO toUserDTO(User user) {
        List<Long> accessGroupIds = user.getAccessGroups().stream()
                .map(AccessGroup::getAccessGroupId)
                .collect(Collectors.toList());

        String profileImageUrl = attachmentService.getAttachmentsForEntity("USER", user.getUserId().toString()).stream()
                .filter(a -> AttachmentType.isImage(a.getFileType()))
                .findFirst()
                .map(attachment -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/users/")
                        .path(user.getUserId().toString())
                        .path("/profile-image")
                        .toUriString())
                .orElse(null);

        return new UserDTO(user.getUserId(), user.getUsername(), profileImageUrl, accessGroupIds);
    }
}
