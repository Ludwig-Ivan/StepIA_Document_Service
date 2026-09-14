package com.stepia.document_service.dto;

import jakarta.validation.constraints.NotBlank;

public record UploadUrlRequest(

                @NotBlank String documentId,

                @NotBlank String filename,

                @NotBlank String contentType

) {
}