package com.stepia.document_service.storage;

public interface StorageService {

    String generateUploadUrl(
            String storageKey,
            String contentType);

    String generateDownloadUrl(
            String storageKey);

    boolean exists(
            String storageKey);

    void delete(
            String storageKey);
}