package com.oop.ticketmasterswiftiebackend.seat.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import com.oop.ticketmasterswiftiebackend.seat.constants.SeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`Seat`")
@EqualsAndHashCode(exclude = "ticketSeats")
@ToString(exclude = "ticketSeats")
public class SeatEntity {
    @Id
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

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "seat", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of ticket seats", example = "[]")
    private TicketSeatEntity ticketSeats;
}
