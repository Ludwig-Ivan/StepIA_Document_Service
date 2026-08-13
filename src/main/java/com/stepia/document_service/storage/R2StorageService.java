package com.stepia.document_service.storage;

import com.stepia.document_service.config.R2Properties;
import com.stepia.document_service.exception.ObjectNotFoundException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class R2StorageService implements StorageService {

        private final S3Client s3Client;
        private final S3Presigner s3Presigner;
        private final R2Properties properties;

        public R2StorageService(
                        S3Client s3Client,
                        S3Presigner s3Presigner,
                        R2Properties properties) {
                this.s3Client = s3Client;
                this.s3Presigner = s3Presigner;
                this.properties = properties;
        }

        @Override
        public String generateUploadUrl(
                        String storageKey,
                        String contentType) {

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(properties.bucket())
                                .key(storageKey)
                                .contentType(contentType)
                                .build();

                PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                                .signatureDuration(Duration.ofMinutes(15))
                                .putObjectRequest(putObjectRequest)
                                .build();

                PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

                return presignedRequest.url().toString();
        }

        @Override
        public String generateDownloadUrl(
                        String storageKey) {

                HeadObjectRequest headRequest = HeadObjectRequest.builder()
                                .bucket(properties.bucket())
                                .key(storageKey)
                                .build();

                try {
                        s3Client.headObject(headRequest);
                } catch (Exception exception) {
                        throw new ObjectNotFoundException(
                                        "El archivo no existe: " + storageKey);
                }

                var getObjectRequest = software.amazon.awssdk.services.s3.model.GetObjectRequest
                                .builder()
                                .bucket(properties.bucket())
                                .key(storageKey)
                                .build();

                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                .signatureDuration(Duration.ofMinutes(15))
                                .getObjectRequest(getObjectRequest)
                                .build();

                PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

                return presignedRequest.url().toString();
        }

        @Override
        public boolean exists(
                        String storageKey) {

                HeadObjectRequest request = HeadObjectRequest.builder()
                                .bucket(properties.bucket())
                                .key(storageKey)
                                .build();

                try {

                        s3Client.headObject(request);

                        return true;

                } catch (Exception exception) {

                        return false;
                }
        }

        @Override
        public void delete(
                        String storageKey) {

                DeleteObjectRequest request = DeleteObjectRequest.builder()
                                .bucket(properties.bucket())
                                .key(storageKey)
                                .build();

                s3Client.deleteObject(request);
        }
}