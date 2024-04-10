package com.oop.ticketmasterswiftiebackend.ticket.models.response;

import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketBookingMethod;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Base ticket response without additional details (e.g., ticket seats)")
public class BaseTicketResponse {
    @Schema(description = "Ticket's ID", example = "1")
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
    @Schema(description = "QR Code Image url")
    private String qrCodeImageUrl;
}
