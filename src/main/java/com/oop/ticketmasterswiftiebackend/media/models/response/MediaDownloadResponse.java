package com.oop.ticketmasterswiftiebackend.media.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MediaDownloadResponse {
    private MediaType mediaType;
    private ByteArrayResource byteArrayResource;
}
