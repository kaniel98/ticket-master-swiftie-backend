package com.oop.ticketmasterswiftiebackend.venue.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PartialUpdateVenueRequest {
    @NotNull(message = "Venue id is required")
    @Schema(description = "Unique identifier for an event", example = "123")
    private Integer venueId;

    @NotEmpty(message = "Venue name is required")
    @Schema(description = "Venue name", example = "Sports Hall 1")
    private String name;

    @NotEmpty(message = "Venue address is required")
    @Schema(description = "Venue address", example = "1234 Main Street")
    private String address;
}
