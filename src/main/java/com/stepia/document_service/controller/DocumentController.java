package com.stepia.document_service.controller;

import com.stepia.document_service.dto.CompleteUploadResponse;
import com.stepia.document_service.dto.DownloadUrlRequest;
import com.stepia.document_service.dto.DownloadUrlResponse;
import com.stepia.document_service.dto.UploadUrlRequest;
import com.stepia.document_service.dto.UploadUrlResponse;
import com.stepia.document_service.service.DocumentStorageService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
public class DocumentController {

        private final DocumentStorageService storageService;

        public DocumentController(
                        DocumentStorageService storageService) {
                this.storageService = storageService;
        }

        @PostMapping("/upload-url")
        public ResponseEntity<UploadUrlResponse> generateUploadUrl(
                        @Valid @RequestBody UploadUrlRequest request) {

                return ResponseEntity.ok(
                                storageService.generateUploadUrl(request));
        }

        @PostMapping("/{documentId}/complete")
        public ResponseEntity<CompleteUploadResponse> completeUpload(
                        @PathVariable String documentId,
                        @RequestParam String storageKey) {

                return ResponseEntity.ok(
                                storageService.completeUpload(
                                                documentId,
                                                storageKey));
        }

        @PostMapping("/download-url")
        public ResponseEntity<DownloadUrlResponse> generateDownloadUrl(
                        @Valid @RequestBody DownloadUrlRequest request) {

                return ResponseEntity.ok(
                                storageService.generateDownloadUrl(
                                                request.storageKey()));
        }

        @DeleteMapping
        public ResponseEntity<Void> delete(
                        @RequestParam String storageKey) {

                storageService.delete(storageKey);

                return ResponseEntity.noContent().build();
        }
}