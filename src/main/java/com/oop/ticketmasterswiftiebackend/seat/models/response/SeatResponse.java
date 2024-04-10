package com.oop.ticketmasterswiftiebackend.seat.models.response;

import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import com.oop.ticketmasterswiftiebackend.seat.constants.SeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SeatResponse {
    @Column(name = "seatId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for Seat", example = "1234")
    private Integer seatId;

    @Schema(description = "Unique identifier for a event venue area", example = "123")
    private Integer eventVenueAreaId;

    @Schema(description = "Seat Column Number", example = "10")
    private Integer seatCol;

    @Schema(description = "Seat Row Number", example = "5")
    private String seatRow;

    @Schema(description = "Category type", example = "CAT1")
    @Enumerated(EnumType.STRING)
    private VenueAreaCategory category;

    @Schema(description = "Seat status", example = "BOOKED")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;
}
