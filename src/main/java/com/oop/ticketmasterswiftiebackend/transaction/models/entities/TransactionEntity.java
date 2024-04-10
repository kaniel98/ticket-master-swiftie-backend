package com.oop.ticketmasterswiftiebackend.transaction.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oop.ticketmasterswiftiebackend.ticket.models.entities.TicketEntity;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionStatus;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`Transaction`")
@EqualsAndHashCode(exclude = {"ticket"})
@ToString(exclude = {"ticket"})
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for transaction", example = "123")
    private Integer transactionId;

    @Schema(description = "Ticket ID", example = "123")
    private Integer ticketId;

    @Schema(description = "Payment Intent Id from Stripe", example = "123")
    private String paymentIntentId;

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

    @ManyToOne(targetEntity = TicketEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId", insertable = false, updatable = false)
    @JsonBackReference
    private TicketEntity ticket;
}
