package com.oop.ticketmasterswiftiebackend.media.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MediaUploadResponse {
    private List<String> fileNames;
}
