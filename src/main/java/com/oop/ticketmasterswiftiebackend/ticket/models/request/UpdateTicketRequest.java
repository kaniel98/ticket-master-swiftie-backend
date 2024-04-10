package com.oop.ticketmasterswiftiebackend.ticket.models.request;

import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateTicketRequest {
    @NotNull(message = "ID of person cancelling the ticket is required")
    @Schema(description = "Ticket's Customer ID", example = "001")
    private Integer userId;

    @NotNull(message = "Ticket ID is required")
    @Schema(description = "Ticket's unique ID", example = "33")
    private Integer ticketId;

    @NotNull(message = "Ticket's Status is required: ACTIVE, EXPIRED, CANCELLED_BY_ORGANISER, CANCELLED_BY_CUSTOMER")
    @Schema(description = "Ticket's Status", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}
