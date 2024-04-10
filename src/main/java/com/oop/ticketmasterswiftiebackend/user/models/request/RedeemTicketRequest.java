package com.oop.ticketmasterswiftiebackend.user.models.request;

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
public class RedeemTicketRequest {
    @Schema(description = "Ticket's unique ID", example = "33")
    @NotNull(message = "Ticket ID is required")
    private Integer ticketId;

    @Schema(description = "Id of the ticket officer redeeming the ticket", example = "001")
    @NotNull(message = "Ticket officer ID is required")
    private Integer ticketOfficerId;
}
