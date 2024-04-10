package com.oop.ticketmasterswiftiebackend.common.constants;

import lombok.Getter;

@Getter
public enum CommonError {
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "Internal server error"),
    BAD_REQUEST(400, "BAD_REQUEST", "Bad request"),
    UNAUTHORIZED(401, "UNAUTHORIZED", "Unauthorized"),
    FORBIDDEN(403, "FORBIDDEN", "Forbidden"),
    NOT_FOUND(404, "NOT_FOUND", "Not found"),
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", "Method not allowed"),
    DATABASE_ERROR(500, "DATABASE_ERROR", "Database error"),
    DATABASE_ERROR_FOREIGN_KEY_CONSTRAINT(500, "DATABASE_ERROR_FOREIGN_KEY_CONSTRAINT", "Database error foreign key constraint");

    private final Integer code;
    private final String businessCode;
    private final String description;

    CommonError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
