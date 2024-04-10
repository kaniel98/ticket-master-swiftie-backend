package com.oop.ticketmasterswiftiebackend.transaction.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionStatus;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateTransactionRequest {
    @NotNull(message = "Ticket ID is required")
    @Schema(description = "Ticket ID", example = "123")
    private Integer ticketId;

    @NotNull(message = "Payment Intent ID is required")
    @Schema(description = "Payment Intent ID", example = "123")
    private String paymentIntentId;

    @NotNull(message = "Transaction Amount is required")
    @Schema(description = "Transaction Amount", example = "100.00")
    private Double amount;

    @NotNull(message = "Transaction Date Time is required")
    @Schema(description = "Transaction Date Time", example = "2022-12-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    @NotNull(message = "Transaction Type is required: REFUND | CHARGE")
    @Schema(description = "Transaction Type", example = "REFUND")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull(message = "Transaction Status is required: SUCCESS | FAILED")
    @Schema(description = "Transaction Status", example = "SUCCESS")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}
