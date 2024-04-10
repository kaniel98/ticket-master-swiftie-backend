package com.oop.ticketmasterswiftiebackend.notification.constants;

import lombok.Getter;

@Getter
public enum NotificationError {
    EMAIL_SEND_ERROR(400, "EMAIL_SEND_ERROR", "Email could not be sent"),
    CONTACT_CREATE_ERROR(400, "CONTACT_CREATE_ERROR", "Contact could not be created");

    private final Integer code;
    private final String businessCode;
    private final String description;

    NotificationError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
