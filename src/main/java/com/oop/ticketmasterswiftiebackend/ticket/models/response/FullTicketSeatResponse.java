package com.oop.ticketmasterswiftiebackend.ticket.models.response;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.EventGroupDetailTicketResponse;
import com.oop.ticketmasterswiftiebackend.transaction.models.response.BaseTransactionResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Full ticket response with additional details (e.g., ticket seats and transactions)")
public class FullTicketSeatResponse extends BaseTicketResponse {
    @Schema(description = "List of ticket seats", example = "[]")
    private List<TicketSeatResponse> ticketSeats;

    @Schema(description = "Transactions related to the ticket", example = "[]")
    private List<BaseTransactionResponse> transactions;

    @Schema(description = "Event group information for ticket")
    private EventGroupDetailTicketResponse eventGroupDetail;
}
