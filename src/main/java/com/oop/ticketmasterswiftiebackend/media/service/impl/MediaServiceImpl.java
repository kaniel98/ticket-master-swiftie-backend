package com.oop.ticketmasterswiftiebackend.media.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.media.constants.MediaError;
import com.oop.ticketmasterswiftiebackend.media.models.response.MediaDownloadResponse;
import com.oop.ticketmasterswiftiebackend.media.models.response.MediaUploadResponse;
import com.oop.ticketmasterswiftiebackend.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final AmazonS3 s3Client;
    private final HashSet<MediaType> allowedContentTypes = new HashSet<>(List.of(
            MediaType.IMAGE_JPEG,
            MediaType.IMAGE_PNG,
            MediaType.IMAGE_GIF
    ));
    private final Duration presignedDuration = Duration.ofDays(7);
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public MediaUploadResponse uploadMedia(MultipartFile[] files) {
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty() || !checkIfContentTypeIsAllowed(file.getContentType())) {
                log.error("Invalid file type");
                throw new BaseException(MediaError.MEDIA_FILE_NOT_ALLOWED.getCode(), MediaError.MEDIA_FILE_NOT_ALLOWED.getDescription(), MediaError.MEDIA_FILE_NOT_ALLOWED.getBusinessCode());
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
                result.add(fileName);
            } catch (Exception e) {
                log.error("Error occurred while uploading media: {}", e.getMessage());
                throw new BaseException(MediaError.MEDIA_UPLOAD_FAILED.getCode(), MediaError.MEDIA_UPLOAD_FAILED.getDescription(), MediaError.MEDIA_UPLOAD_FAILED.getBusinessCode());
            }
        }
        return MediaUploadResponse.builder().fileNames(result).build();
    }

    @Override
    public String uploadQrCodeImage(String folder, ByteArrayResource file, MediaType contentType) {

        String prefix = "";
        if (StringUtils.hasLength(folder)) {
            prefix = folder + "/";
        }

        String fileName = prefix + UUID.randomUUID() + ".png";
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType.toString());
        metadata.setContentLength(file.contentLength());

        try (InputStream inputStream = file.getInputStream()) {
            log.info("Uploading QRCode image to S3 bucket");
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

            log.info("Generating pre-signed URL for the uploaded QRCode image");
            // Generate pre-signed url for this
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(new Date(System.currentTimeMillis() + presignedDuration.toMillis()));

            return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception e) {
            log.error("Error occurred while uploading media: {}", e.getMessage());
            throw new BaseException(MediaError.MEDIA_UPLOAD_FAILED.getCode(), MediaError.MEDIA_UPLOAD_FAILED.getDescription(), MediaError.MEDIA_UPLOAD_FAILED.getBusinessCode());
        }
    }

    // * InputStreamResource is used over raw InputStream - Prevent implementation details from being exposed
    // * ByteArrayOutput stream holds the entire file in memory - Not ideal
    @Override
    public MediaDownloadResponse getMedia(String fileName) {
        // Check if file exists
        boolean mediaExist = s3Client.doesObjectExist(bucketName, fileName);
        if (!mediaExist) {
            throw new BaseException(MediaError.MEDIA_NOT_FOUND.getCode(), MediaError.MEDIA_NOT_FOUND.getBusinessCode(), MediaError.MEDIA_NOT_FOUND.getDescription());
        }

        try {
            S3Object media = s3Client.getObject(bucketName, fileName);
            S3ObjectInputStream mediaContent = media.getObjectContent();
            MediaType mediaType = MediaType.valueOf(media.getObjectMetadata().getContentType());

            return MediaDownloadResponse.builder()
                    .mediaType(mediaType)
                    .byteArrayResource(new ByteArrayResource(mediaContent.readAllBytes()))
                    .build();

        } catch (AmazonS3Exception exception) {
            log.error("Error occurred while getting media: {}", exception.getMessage());
            throw new BaseException(MediaError.MEDIA_GET_FAILED.getCode(), MediaError.MEDIA_GET_FAILED.getDescription(), MediaError.MEDIA_GET_FAILED.getBusinessCode());
        } catch (Exception exception) {
            log.error("Error occurred while getting media: {}", exception.getMessage());
            throw CommonUtils.commonExceptionHandler(exception);
        }
    }

    @Override
    public void deleteMedia(List<String> fileNames) {
        List<DeleteObjectsRequest.KeyVersion> keys = fileNames.stream()
                .map(DeleteObjectsRequest.KeyVersion::new)
                .toList();

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys);
        DeleteObjectsResult deleteObjectsResult = s3Client.deleteObjects(deleteObjectsRequest);
        // Asset successfully deleted
        int successfulDeletes = deleteObjectsResult.getDeletedObjects().size();
        if (successfulDeletes != fileNames.size()) {
            log.info("Some files were not deleted");
            return;
        }
        log.info("Successfully deleted {} files", successfulDeletes);
    }

    private boolean checkIfContentTypeIsAllowed(String contentType) {
        return allowedContentTypes.contains(MediaType.valueOf(contentType));
    }

}
