package com.oop.ticketmasterswiftiebackend.venue.models.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Venue with Venue Area details only")
public class VenueWithSeatMapResponse extends BaseVenueResponse {
    @Schema(description = "List of venue areas", example = "[]")
    private List<VenueAreaResponse> seatMap;
}
