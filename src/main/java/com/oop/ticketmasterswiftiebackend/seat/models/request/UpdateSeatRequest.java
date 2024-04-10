package com.oop.ticketmasterswiftiebackend.seat.models.request;

import com.oop.ticketmasterswiftiebackend.seat.constants.SeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateSeatRequest {
    @NotNull(message = "Seat's ID is required")
    @Schema(description = "Unique identifier for Seat", example = "1234")
    private List<Integer> seatIds;

    @NotNull(message = "Seat's Status is required: AVAILABLE, BOOKED, NOT_OPEN")
    @Schema(description = "Seat status", example = "BOOKED")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;
}
