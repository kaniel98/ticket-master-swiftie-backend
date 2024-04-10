package com.oop.ticketmasterswiftiebackend.ticket.models.response;

import com.oop.ticketmasterswiftiebackend.seat.models.response.SeatResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TicketSeatResponse {
    private SeatResponse seat;
}
