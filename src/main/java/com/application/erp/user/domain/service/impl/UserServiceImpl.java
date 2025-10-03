package com.application.erp.user.domain.service.impl;

import com.application.erp.shared.config.AttachmentType;
import com.application.erp.user.dto.request.CreateUserDto;
import com.application.erp.user.dto.request.UpdateUserDto;
import com.application.erp.user.dto.response.UserDTO;
import com.application.erp.user.dto.UserFilter;
import com.application.erp.accessgroup.domain.entity.AccessGroup;
import com.application.erp.attachment.domain.entity.Attachment;
import com.application.erp.user.domain.entity.User;
import com.application.erp.shared.exception.EntityNotFoundException;
import com.application.erp.user.exception.UserAlreadyExistsException;
import com.application.erp.accessgroup.domain.repository.AccessGroupRepository;
import com.application.erp.user.domain.repository.UserRepository;
import com.application.erp.attachment.domain.service.AttachmentService;
import com.application.erp.user.domain.service.UserService;
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
                    throw new UserAlreadyExistsException("A user with this username already exists", "username");
                });

        userRepository.findByEmail(createUserDto.email())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("A user with this email already exists", "email");
                });

        User user = new User();
        user.setUsername(createUserDto.username());
        user.setEmail(createUserDto.email());
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
        return userRepository.findUsersByFilter(userFilter, pageable)
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

        if (updateUserDto.email() != null && !updateUserDto.email().isBlank()) {
            user.setEmail(updateUserDto.email());
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

        return new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(), profileImageUrl, accessGroupIds);
    }
}
