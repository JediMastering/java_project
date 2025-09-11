package com.example.first.web;

import com.example.first.dto.CreateUserDto;
import com.example.first.dto.UpdateUserDto;
import com.example.first.dto.UserDTO;
import com.example.first.dto.UserFilter;
import com.example.first.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        UserDTO createdUser = userService.createUser(createUserDto);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(UserFilter userFilter, Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(userFilter, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID userId, @RequestBody @Valid UpdateUserDto updateUserDto) {
        UserDTO updatedUser = userService.updateUser(userId, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}