package com.oop.ticketmasterswiftiebackend.venue.models.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Base venue response without additional details (e.g., venue area)")

public class BaseVenueResponse {
    @Id
    @Schema(description = "Unique identifier for an event", example = "123")
    private Integer venueId;

    @Schema(description = "Venue name", example = "Sports Hall 1")
    private String name;

    @Schema(description = "Venue address", example = "1234 Main Street")
    private String address;
}
