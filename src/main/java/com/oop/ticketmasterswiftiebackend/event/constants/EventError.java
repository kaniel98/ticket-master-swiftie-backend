package com.oop.ticketmasterswiftiebackend.event.constants;

import lombok.Getter;

@Getter
public enum EventError {
    DUPLICATE_VENUE(409, "DUPLICATE_VENUE", "Venue with the same name / address exists"),
    VENUE_NOT_FOUND(400, "VENUE_NOT_FOUND", "Venue not found in database"),
    VENUE_AREA_NOT_FOUND(400, "VENUE_AREA_NOT_FOUND", "Venue area not found in database"),
    EVENT_GROUP_NOT_FOUND(404, "EVENT_GROUP_NOT_FOUND", "Event group not found in database"),
    EVENT_GROUP_DETAIL_NOT_FOUND(404, "EVENT_GROUP_DETAIL_NOT_FOUND", "Event group detail not found in database"),
    EVENT_GROUP_NOT_EDITABLE(400, "EVENT_GROUP_NOT_EDITABLE", "Event group is not editable"),
    EVENT_STATUS_UPDATE_RULE_VIOLATION(400, "EVENT_STATUS_UPDATE_RULE_VIOLATION", "Event status update rule violation"),
    EVENT_ALREADY_CANCELED(400, "EVENT_CANNOT_CANCEL", "Event has either been cancelled or passed"),


    ;

    private final Integer code;
    private final String businessCode;
    private final String description;

    EventError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
