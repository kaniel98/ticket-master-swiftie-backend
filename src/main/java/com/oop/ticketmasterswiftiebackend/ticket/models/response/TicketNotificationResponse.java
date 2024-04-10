package com.oop.ticketmasterswiftiebackend.ticket.models.response;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.EventGroupDetailNotificationResponse;
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
public class TicketNotificationResponse extends BaseTicketResponse {
    @Schema(description = "List of ticket seats", example = "[]")
    private List<TicketSeatResponse> ticketSeats;

    @Schema(description = "Event group information for ticket")
    private EventGroupDetailNotificationResponse eventGroupDetail;
}
