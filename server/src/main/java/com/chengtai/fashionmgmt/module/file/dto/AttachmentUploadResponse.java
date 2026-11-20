package com.chengtai.fashionmgmt.module.file.dto;

public record AttachmentUploadResponse(
        String originalName,
        String storedName,
        String relativePath,
        String url,
        long size,
        String contentType
) {
}
