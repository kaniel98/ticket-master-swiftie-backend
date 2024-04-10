package com.oop.ticketmasterswiftiebackend.seat.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.oop.ticketmasterswiftiebackend.ticket.models.entities.TicketEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`TicketSeat`")
@EqualsAndHashCode(exclude = {"ticket", "seat"})
@ToString(exclude = {"ticket", "seat"})
public class TicketSeatEntity {
    @Id
    @Column(name = "ticketSeatId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for TicketSeat", example = "1234")
    private Integer ticketSeatId;

    @Schema(description = "Unique identifier for Ticket", example = "1234")
    private Integer ticketId;

    @Schema(description = "Unique identifier for Seat", example = "1234")
    private Integer seatId;

    @ManyToOne(targetEntity = TicketEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ticketId", insertable = false, updatable = false)
    @JsonBackReference
    private TicketEntity ticket;

    @OneToOne(targetEntity = SeatEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "seatId", insertable = false, updatable = false)
    @JsonBackReference
    private SeatEntity seat;
}
