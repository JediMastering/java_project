package com.example.first.config;

import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public enum AttachmentType {
    PDF("application/pdf", MediaType.APPLICATION_PDF),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document")),
    JPG("image/jpeg", MediaType.IMAGE_JPEG),
    PNG("image/png", MediaType.IMAGE_PNG);

    private final String mimeType;
    private final MediaType mediaType;

    AttachmentType(String mimeType, MediaType mediaType) {
        this.mimeType = mimeType;
        this.mediaType = mediaType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public static Optional<AttachmentType> fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(type -> type.getMimeType().equalsIgnoreCase(mimeType))
                .findFirst();
    }

    public static Optional<AttachmentType> fromName(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public static boolean isImage(String name) {
        return fromName(name)
                .map(type -> Set.of(JPG, PNG).contains(type))
                .orElse(false);
    }
}
