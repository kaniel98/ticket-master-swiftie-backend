package com.oop.ticketmasterswiftiebackend.seat.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request to assign a seat based on event venue area id and the number of seats")
public class CreateSeatRequest {
    @NotNull(message = "eventVenueAreaId is required")
    @Schema(description = "Unique identifier for a event venue area", example = "123")
    private Integer eventVenueAreaId;

    @NotNull(message = "Number of seats must be provided")
    @Schema(description = "Number of seats")
    @Max(4)
    @Min(1)
    private Integer numberOfSeats;
}
