package com.application.erp.user.controller;

import com.application.erp.shared.config.AttachmentType;
import com.application.erp.user.dto.request.CreateUserDto;
import com.application.erp.user.dto.request.UpdateUserDto;
import com.application.erp.user.dto.response.UserDTO;
import com.application.erp.user.dto.UserFilter;
import com.application.erp.attachment.domain.entity.Attachment;
import com.application.erp.attachment.domain.service.AttachmentService;
import com.application.erp.user.domain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @PreAuthorize("hasPermission('users', 'create')")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        UserDTO createdUser = userService.createUser(createUserDto);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    @PreAuthorize("hasPermission('users', 'view')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(UserFilter userFilter, Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(userFilter, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasPermission('users', 'view')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/profile-image")
    @PreAuthorize("hasPermission('users', 'view')")
    public ResponseEntity<Resource> getUserProfileImage(@PathVariable Long userId) {
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
    @PreAuthorize("hasPermission('users', 'edit')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody @Valid UpdateUserDto updateUserDto) {
        UserDTO updatedUser = userService.updateUser(userId, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasPermission('users', 'delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
