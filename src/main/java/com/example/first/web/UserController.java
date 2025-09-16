package com.example.first.web;

import com.example.first.config.AttachmentType;
import com.example.first.dto.CreateUserDto;
import com.example.first.dto.UpdateUserDto;
import com.example.first.dto.UserDTO;
import com.example.first.dto.UserFilter;
import com.example.first.entity.Attachment;
import com.example.first.service.AttachmentService;
import com.example.first.service.UserService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AttachmentService attachmentService;

    public UserController(UserService userService, AttachmentService attachmentService) {
        this.userService = userService;
        this.attachmentService = attachmentService;
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

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<Resource> getUserProfileImage(@PathVariable UUID userId) {
        List<Attachment> attachments = attachmentService.getAttachmentsForEntity("USER", userId.toString());

        Optional<Attachment> profileImage = attachments.stream()
                .filter(a -> AttachmentType.isImage(a.getFileType()))
                .findFirst();

        if (profileImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Attachment attachment = profileImage.get();
        Resource resource = attachmentService.loadFileAsResource(attachment.getId());

        MediaType mediaType = AttachmentType.fromName(attachment.getFileType())
                .map(AttachmentType::getMediaType)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
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
