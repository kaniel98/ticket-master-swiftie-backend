package com.oop.ticketmasterswiftiebackend.media.constants;

import lombok.Getter;

@Getter
public enum MediaError {
    MEDIA_UPLOAD_FAILED(500, "MEDIA_UPLOAD_FAILED", "Media upload failed"),
    MEDIA_GET_FAILED(500, "MEDIA_GET_FAILED", "Get media failed"),
    MEDIA_NOT_FOUND(404, "MEDIA_NOT_FOUND", "Media not found"),
    MEDIA_DELETE_FAILED(500, "MEDIA_DELETE_FAILED", "Media delete failed"),
    MEDIA_NOT_AUTHORIZED(401, "MEDIA_NOT_AUTHORIZED", "User not authorized to perform this action"),
    MEDIA_FILE_EMPTY(400, "MEDIA_FILE_EMPTY", "Media file is empty"),
    MEDIA_FILE_NOT_ALLOWED(400, "MEDIA_FILE_NOT_ALLOWED", "Media file type not allowed");

    private final Integer code;
    private final String businessCode;
    private final String description;

    MediaError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
