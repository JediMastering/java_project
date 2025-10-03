package com.application.erp.attachment.domain.service.impl;

import com.application.erp.shared.config.AttachmentType;
import com.application.erp.shared.config.FileStorageProperties;
import com.application.erp.attachment.domain.entity.Attachment;
import com.application.erp.shared.exception.EntityNotFoundException;
import com.application.erp.attachment.domain.repository.AttachmentRepository;
import com.application.erp.attachment.domain.service.AttachmentService;
import com.application.erp.shared.utils.FileNameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final AttachmentRepository attachmentRepository;
    private final Path fileStorageLocation;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, FileStorageProperties fileStorageProperties) {
        this.attachmentRepository = attachmentRepository;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    @Transactional
    public Attachment storeFile(MultipartFile file, String entityType, String entityId, boolean useRandomName, Set<AttachmentType> allowedTypes) throws IOException {
        String mimeType = file.getContentType();
        Set<AttachmentType> validationTypes = (allowedTypes == null || allowedTypes.isEmpty())
                ? EnumSet.allOf(AttachmentType.class)
                : allowedTypes;

        AttachmentType attachmentType = AttachmentType.fromMimeType(mimeType)
                .orElseThrow(() -> new IllegalArgumentException("File type not supported: " + mimeType));

        if (!validationTypes.contains(attachmentType)) {
            throw new IllegalArgumentException("File type not allowed for this operation: " + mimeType);
        }

        // Regra de negócio: Se for uma imagem de usuário, apagar a antiga.
        if ("USER".equalsIgnoreCase(entityType) && AttachmentType.isImage(attachmentType.name())) {
            List<Attachment> oldImages = getAttachmentsForEntity(entityType, entityId).stream()
                    .filter(a -> AttachmentType.isImage(a.getFileType()))
                    .toList();
            for (Attachment oldImage : oldImages) {
                deleteAttachment(oldImage.getId());
            }
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        
        String storedFileName;
        if (useRandomName) {
            storedFileName = UUID.randomUUID().toString() + "." + fileExtension;
        } else {
            String baseName = StringUtils.stripFilenameExtension(originalFileName);
            String timestamp = String.valueOf(System.currentTimeMillis());
            storedFileName = FileNameUtils.sanitize(baseName) + "-" + timestamp + "." + fileExtension;
        }

        LocalDate now = LocalDate.now();
        String yearMonthPath = String.format("%d/%02d", now.getYear(), now.getMonthValue());
        Path targetPath = this.fileStorageLocation.resolve(yearMonthPath);

        Files.createDirectories(targetPath);

        Path targetLocation = targetPath.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        Attachment attachment = new Attachment();
        attachment.setOriginalFileName(originalFileName);
        attachment.setStoredFileName(storedFileName);
        attachment.setPath("attachments/" + yearMonthPath);
        attachment.setFileType(attachmentType.name());
        attachment.setEntityType(entityType.toUpperCase());
        attachment.setEntityId(entityId);

        return attachmentRepository.save(attachment);
    }

    @Override
    public List<Attachment> getAttachmentsForEntity(String entityType, String entityId) {
        logger.info("Searching attachments for entityType: {} and entityId: {}", entityType, entityId);
        List<Attachment> attachments = attachmentRepository.findByEntityTypeAndEntityId(entityType.toUpperCase(), entityId);
        logger.info("Found {} attachments for entityType: {} and entityId: {}. Attachments: {}", attachments.size(), entityType, entityId, attachments);
        return attachments;
    }

    @Override
    public Resource loadFileAsResource(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found with id: " + attachmentId));

        try {
            Path filePath = this.fileStorageLocation
                    .resolve(attachment.getPath().replace("attachments/", ""))
                    .resolve(attachment.getStoredFileName()).normalize();
            
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + attachment.getStoredFileName());
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + attachment.getStoredFileName(), ex);
        }
    }
    
    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId) throws IOException {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found with id: " + attachmentId));

        Path filePath = this.fileStorageLocation
                .resolve(attachment.getPath().replace("attachments/", ""))
                .resolve(attachment.getStoredFileName()).normalize();
        
        Files.deleteIfExists(filePath);

        attachmentRepository.delete(attachment);
    }
}
