package com.application.erp.attachment.controller;

import com.application.erp.attachment.domain.entity.Attachment;
import com.application.erp.attachment.domain.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Attachment> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("entityType") String entityType,
            @RequestParam("entityId") String entityId,
            @RequestParam(value = "useRandomName", defaultValue = "false") boolean useRandomName) {
        try {
            Attachment attachment = attachmentService.storeFile(file, entityType, entityId, useRandomName, null);
            return ResponseEntity.ok(attachment);
        } catch (IOException ex) {
            return ResponseEntity.status(500).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<Attachment>> getAttachmentsForEntity(
            @PathVariable String entityType,
            @PathVariable String entityId) {

        List<Attachment> attachments = attachmentService.getAttachmentsForEntity(entityType, entityId);

        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long attachmentId) {
        Resource resource = attachmentService.loadFileAsResource(attachmentId);

        String contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        try {
            attachmentService.deleteAttachment(attachmentId);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}