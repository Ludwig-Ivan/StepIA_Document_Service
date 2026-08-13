package com.stepia.document_service.dto;

public record UploadUrlResponse(

        String documentId,

        String storageKey,

        String uploadUrl,

        long expiresInSeconds

) {
}