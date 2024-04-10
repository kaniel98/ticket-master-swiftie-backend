package com.oop.ticketmasterswiftiebackend.venue.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request body required to create a venue")
public class CreateVenueRequest {
    @NotEmpty(message = "Venue name is required")
    @Schema(description = "Venue name", example = "Sports Hall 1")
    private String name;

    @NotEmpty(message = "Venue address is required")
    @Schema(description = "Venue address", example = "1234 Main Street")
    private String address;

    @Schema(description = "List of venue areas", example = "[]")
    @Valid
    private List<VenueAreaRequest> seatMap;
}
