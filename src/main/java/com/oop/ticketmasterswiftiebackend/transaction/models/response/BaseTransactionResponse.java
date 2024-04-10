package com.oop.ticketmasterswiftiebackend.transaction.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionStatus;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class BaseTransactionResponse {
    @Schema(description = "Transaction amount", example = "100.00")
    private Double amount;

    @Schema(description = "Transaction date time", example = "2022-12-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    @Schema(description = "Transaction type", example = "REFUND")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Schema(description = "Transaction status", example = "SUCCESS")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}
