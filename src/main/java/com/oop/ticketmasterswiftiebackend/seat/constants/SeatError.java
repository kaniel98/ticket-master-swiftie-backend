package com.oop.ticketmasterswiftiebackend.seat.constants;

import lombok.Getter;

@Getter

public enum SeatError {
    SEAT_NOT_FOUND(404, "SEAT_NOT_FOUND", "Seat not found"),
    SEAT_STATUS_SAME(400, "SEAT_STATUS_SAME", "Current Seat status and status to update are the same"),
    SEAT_ALREADY_EXIST(400, "SEAT_ALREADY_EXIST", "There is already a seat in that spot"),
    SEAT_POSITION_INVALID(400, "SEAT_POSITION_INVALID", "The seat position (row: %s, column: %s) is invalid"),
    SEAT_NOT_ENOUGH(400, "SEAT_NOT_ENOUGH", "There are not enough seats available, please choose another category"),
    SEAT_CREATE_FAILED(500, "SEAT_CREATE_FAILED", "Failed to create seat");


    private final Integer code;
    private final String businessCode;
    private final String description;

    SeatError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
