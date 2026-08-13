package com.stepia.document_service.dto;

public record CompleteUploadResponse(

                String documentId,

                String storageKey,

                boolean exists

) {
}