package com.oop.ticketmasterswiftiebackend.ticket.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupDetailEntity;
import com.oop.ticketmasterswiftiebackend.seat.models.entities.TicketSeatEntity;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketBookingMethod;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketStatus;
import com.oop.ticketmasterswiftiebackend.transaction.models.entities.TransactionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`Ticket`")
@EqualsAndHashCode(exclude = {"ticketSeats", "transactions"})
@ToString(exclude = {"ticketSeats", "transactions"})
public class TicketEntity {
    @Id
    @Column(name = "ticketId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for Ticket", example = "1234")
    private Integer ticketId;

    @Schema(description = "Ticket's Event ID", example = "123")
    private Integer eventGroupDetailId;

    @Schema(description = "Ticket's Customer ID", example = "001")
    private Integer customerId;

    @Schema(description = "ticket availability status: ACTIVE, EXPIRED, CANCELLED_BY_ORGANISER, CANCELLED_BY_CUSTOMER", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Schema(description = "booking method: ONSITE | ONLINE", example = "ONLINE")
    @Enumerated(EnumType.STRING)
    private TicketBookingMethod bookingMethod;

    @Schema(description = "number of guests <= 4", example = "3")
    private Integer numberOfGuests;

    @Schema(description = "Image URL for the ticket's QR Code")
    private String qrCodeImageUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticket", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of ticket seats", example = "[]")
    private List<TicketSeatEntity> ticketSeats;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TransactionEntity> transactions;

    @ManyToOne(targetEntity = EventGroupDetailEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "eventGroupDetailId", insertable = false, updatable = false)
    @JsonBackReference
    private EventGroupDetailEntity eventGroupDetail;
}
