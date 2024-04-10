package com.oop.ticketmasterswiftiebackend.transaction.constants;

import lombok.Getter;

@Getter
public enum TransactionError {
    DUPLICATE_TRANSACTION(409, "DUPLICATE_VENUE", "Transaction with the same payment intent id exists"),
    TRANSACTION_NOT_FOUND(400, "TRANSACTION_NOT_FOUND", "Transaction not found in database"), TRANSACTION_REFUND_ERROR(400, "TRANSACTION_REFUND_ERROR", "Error occurred while refunding transaction");

    private final Integer code;
    private final String businessCode;
    private final String description;

    TransactionError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
