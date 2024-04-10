package com.oop.ticketmasterswiftiebackend.venue.models.response;

import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Venue area details for a given venue and event id")
public class BaseEventVenueAreaResponse {
    @Schema(description = "Event venue area id", example = "1")
    private Integer eventVenueAreaId;

    @Schema(description = "Category type", example = "CAT1")
    @Enumerated(EnumType.STRING)
    private VenueAreaCategory category;

    @Schema(description = "Number of seats", example = "300")
    private Integer noOfSeat;

    @Schema(description = "Number of columns", example = "3")
    private Integer noOfCol;

    @Schema(description = "Number of rows", example = "3")
    private Integer noOfRow;

    @Schema(description = "Price for given category", example = "100.00")
    private Double price;

    @Schema(description = "Position identifier for the given venue area", example = "CAT1_1")
    private String position;
}
