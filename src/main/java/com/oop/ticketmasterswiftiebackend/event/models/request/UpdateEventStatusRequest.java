package com.oop.ticketmasterswiftiebackend.event.models.request;

import com.oop.ticketmasterswiftiebackend.event.constants.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateEventStatusRequest {
    @NotNull(message = "Event id is required")
    @Schema(description = "Unique identifier for an event", example = "123")
    private Integer eventGroupId;

    @NotNull(message = "Event status is required")
    @Schema(description = "Event status", example = "OPENED")
    private EventStatus status;

    @NotNull(message = "Booking allowed status is required")
    @Schema(description = "Indicate if event is open for booking", example = "true")
    private Boolean isBookingAllowed;
}
