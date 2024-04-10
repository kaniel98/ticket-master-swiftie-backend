package com.oop.ticketmasterswiftiebackend.user.constants;

import lombok.Getter;

@Getter
public enum UserError {
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "User not found"),
    USER_ALREADY_EXISTS(409, "USER_ALREADY_EXISTS", "Email or Username is already in use"),
    USER_NOT_AUTHORIZED(401, "USER_NOT_AUTHORIZED", "User not authorized to perform this action"),
    CUSTOMER_CANCEL_ERROR(400, "CUSTOMER_CANCEL_ERROR", "Customer cannot cancel ticket"),
    CUSTOMER_TICKET_ERROR(409, "CUSTOMER_TICKET_ERROR", "Customer does not have access to this ticket");

    private final Integer code;
    private final String businessCode;
    private final String description;

    UserError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
