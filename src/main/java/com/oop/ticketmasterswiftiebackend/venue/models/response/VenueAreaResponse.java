package com.oop.ticketmasterswiftiebackend.venue.models.response;

import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Venue area details for a given venue")
public class VenueAreaResponse {
    @Schema(description = "Number of seats", example = "300")
    private Integer noOfSeat;

    @Schema(description = "Number of columns", example = "3")
    private Integer noOfCol;

    @Schema(description = "Number of rows", example = "3")
    private Integer noOfRow;

    @Schema(description = "Unique identifier for a venue id", example = "123")
    private Integer venueId;

    @Schema(description = "Category type", example = "CAT1")
    private VenueAreaCategory category;
    
    @Schema(description = "Position identifier for the given venue area", example = "CAT1_1")
    private String position;
}
