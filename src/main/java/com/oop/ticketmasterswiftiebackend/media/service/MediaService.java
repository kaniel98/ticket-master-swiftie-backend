package com.oop.ticketmasterswiftiebackend.media.service;

import com.oop.ticketmasterswiftiebackend.media.models.response.MediaDownloadResponse;
import com.oop.ticketmasterswiftiebackend.media.models.response.MediaUploadResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    MediaUploadResponse uploadMedia(MultipartFile[] files);

    String uploadQrCodeImage(String folder, ByteArrayResource file, MediaType contentType);

    MediaDownloadResponse getMedia(String key);

    void deleteMedia(List<String> fileNames);
}
