package com.oop.ticketmasterswiftiebackend.transaction.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionStatus;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionResponse {
    @Schema(description = "Ticket id that this transaction belongs to", example = "1")
    private BaseTicketResponse ticketId;

    @Schema(description = "Payment Intent Id from Stripe", example = "123")
    private String paymentIntentId;

    @Schema(description = "Transaction amount", example = "100.00")
    private Double amount;

    @Schema(description = "Transaction date time", example = "2022-12-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    @Schema(description = "Transaction type", example = "REFUND")
    private TransactionType type;

    @Schema(description = "Transaction status", example = "SUCCESS")
    private TransactionStatus status;
}
