package com.oop.ticketmasterswiftiebackend.venue.constants;

public enum VenueError {
    DUPLICATE_VENUE(409, "DUPLICATE_VENUE", "Venue with the same name / address exists"),
    VENUE_NOT_FOUND(400, "VENUE_NOT_FOUND", "Venue not found in database"),
    VENUE_AREA_NOT_FOUND(400, "VENUE_AREA_NOT_FOUND", "Venue area not found in database");
    private final Integer code;
    private final String businessCode;
    private final String description;

    VenueError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
