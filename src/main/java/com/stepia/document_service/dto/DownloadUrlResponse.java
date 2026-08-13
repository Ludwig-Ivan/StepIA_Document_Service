package com.stepia.document_service.dto;

public record DownloadUrlResponse(

        String storageKey,

        String downloadUrl,

        long expiresInSeconds

) {
}