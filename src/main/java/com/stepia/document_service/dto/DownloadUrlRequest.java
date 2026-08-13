package com.stepia.document_service.dto;

import jakarta.validation.constraints.NotBlank;

public record DownloadUrlRequest(

                @NotBlank String storageKey

) {
}