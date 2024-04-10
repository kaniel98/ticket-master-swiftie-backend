package com.oop.ticketmasterswiftiebackend.ticket.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeleteTicketRequest {
    @NotNull(message = "Ticket ID is required")
    @Schema(description = "Ticket's unique ID", example = "33")
    private Integer ticketId;

    @NotNull(message = "Event Group Detail ID is required")
    @Schema(description = "Ticket's Event Group Detail ID", example = "123")
    private Integer eventGroupDetailId;

    @NotNull(message = "Customer ID is required")
    @Schema(description = "Ticket's Customer ID", example = "001")
    private Integer customerId;
}
