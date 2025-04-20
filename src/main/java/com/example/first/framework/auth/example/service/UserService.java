package com.example.first.framework.auth.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.first.framework.auth.example.dto.UserRequestDto;
import com.example.first.framework.auth.example.dto.UserResponseDto;
import com.example.first.framework.auth.example.exception.BusinessException;
import com.example.first.framework.auth.example.exception.NotFoundException;
import com.example.first.framework.auth.example.model.UserExample;
import com.example.first.framework.auth.example.repository.UserExampleRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserExampleRepository userRepository;

    public UserResponseDto create(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email already in use");
        }

        UserExample user = UserExample.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();

        return toDto(userRepository.save(user));
    }

    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toDto);
    }

    public UserResponseDto findById(Long id) {
        UserExample user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("UserExample not found"));
        return toDto(user);
    }

    public UserResponseDto update(Long id, UserRequestDto dto) {
        UserExample user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("UserExample not found"));

        if (!user.getEmail().equals(dto.getEmail()) &&
            userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email already in use");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return toDto(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("UserExample not found");
        }
        userRepository.deleteById(id);
    }

    private UserResponseDto toDto(UserExample user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
