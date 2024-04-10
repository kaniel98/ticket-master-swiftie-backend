package com.oop.ticketmasterswiftiebackend.external.constants;

import lombok.Getter;

@Getter
public enum QRCodeGeneratorError {
    ERROR_GENERATING_QRCODE(400, "ERROR_GENERATING_QRCODE", "Error occurred when generating QR Code");


    private final Integer code;
    private final String businessCode;
    private final String description;

    QRCodeGeneratorError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
