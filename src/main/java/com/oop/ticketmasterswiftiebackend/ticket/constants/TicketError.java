package com.oop.ticketmasterswiftiebackend.ticket.constants;

import lombok.Getter;

@Getter
public enum TicketError {
    TICKET_NOT_FOUND(404, "TICKET_NOT_FOUND", "Ticket not found"),
    TICKET_ALREADY_EXISTS(409, "TICKET_ALREADY_EXISTS", "Ticket already exists"),
    TICKET_ALREADY_CANCELLED(400, "TICKET_ALREADY_CANCELLED", "Ticket already cancelled"),
    TICKET_CUSTOMER_ID_AND_EVENT_ID_NOT_FOUND(400, "TICKET_CUSTOMER_ID_AND_EVENT_ID_NOT_FOUND", "Ticket customerId and eventId not found"),
    TICKET_CUSTOMER_ID_NOT_FOUND(400, "TICKET_CUSTOMER_ID_NOT_FOUND", "Ticket customer id not found"),
    TICKET_EVENT_ID_NOT_FOUND(400, "TICKET_EVENT_ID_NOT_FOUND", "Ticket event id not found"),
    TICKET_GUEST_EXCEED_MAXIMUM(400, "TICKET_GUEST_EXCEED_MAXIMUM", "Ticket number of guest cannot exceed 4"),
    TICKET_SEATS_NOT_EQUAL_NUM_OF_GUEST(400, "TICKET_SEATS_NOT_EQUAL_NUM_OF_GUEST", "Ticket seats should be equal to number of guest(s)"),
    TICKET_CANNOT_BE_CANCELLED(400, "TICKET_CANNOT_BE_CANCELLED", "Ticket cannot be cancelled"),
    TICKET_NOT_VALID_FOR_EVENT(400, "TICKET_NOT_VALID_FOR_EVENT", "Ticket is not valid for the event"),
    TICKET_NOT_ACTIVE(400, "TICKET_NOT_ACTIVE", "Ticket is not active");


    private final Integer code;
    private final String businessCode;
    private final String description;

    TicketError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
