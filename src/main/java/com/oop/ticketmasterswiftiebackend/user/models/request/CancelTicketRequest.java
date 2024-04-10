package com.oop.ticketmasterswiftiebackend.user.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CancelTicketRequest {
    @NotNull(message = "ID of the person who is cancelling the ticket is required")
    @Schema(description = "Canceller ID", example = "001")
    private Integer userId;

    @NotNull(message = "Customer ID is required")
    @Schema(description = "Ticket's Customer ID", example = "001")
    private Integer customerId;

    @NotNull(message = "Ticket ID is required")
    @Schema(description = "Ticket's unique ID", example = "33")
    private Integer ticketId;
}
