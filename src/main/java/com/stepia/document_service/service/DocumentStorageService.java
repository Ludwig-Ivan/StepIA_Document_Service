package com.stepia.document_service.service;

import com.stepia.document_service.dto.CompleteUploadResponse;
import com.stepia.document_service.dto.DownloadUrlResponse;
import com.stepia.document_service.dto.UploadUrlRequest;
import com.stepia.document_service.dto.UploadUrlResponse;
import com.stepia.document_service.storage.StorageService;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DocumentStorageService {

    private static final long URL_EXPIRATION_SECONDS = 900;

    private final StorageService storageService;

    public DocumentStorageService(
            StorageService storageService) {
        this.storageService = storageService;
    }

    public UploadUrlResponse generateUploadUrl(
            UploadUrlRequest request) {

        String safeFilename = sanitizeFilename(request.filename());

        String storageKey = "clinical-documents/"
                + request.documentId()
                + "/"
                + UUID.randomUUID()
                + "-"
                + safeFilename;

        String uploadUrl = storageService.generateUploadUrl(
                storageKey,
                request.contentType());

        return new UploadUrlResponse(
                request.documentId(),
                storageKey,
                uploadUrl,
                URL_EXPIRATION_SECONDS);
    }

    public CompleteUploadResponse completeUpload(
            String documentId,
            String storageKey) {

        boolean exists = storageService.exists(storageKey);

        return new CompleteUploadResponse(
                documentId,
                storageKey,
                exists);
    }

    public DownloadUrlResponse generateDownloadUrl(
            String storageKey) {

        String url = storageService.generateDownloadUrl(
                storageKey);

        return new DownloadUrlResponse(
                storageKey,
                url,
                URL_EXPIRATION_SECONDS);
    }

    public void delete(
            String storageKey) {

        storageService.delete(storageKey);
    }

    private String sanitizeFilename(
            String filename) {

        return filename
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}