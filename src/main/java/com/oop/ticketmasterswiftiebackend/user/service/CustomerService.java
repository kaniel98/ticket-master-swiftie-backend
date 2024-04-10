package com.oop.ticketmasterswiftiebackend.user.service;

import com.oop.ticketmasterswiftiebackend.ticket.models.request.BookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelTicketRequest;

import java.util.List;

public interface CustomerService {
    List<TicketWithSeatsResponse> getTicketsByCustomerId(Integer customerId);

    BaseTicketResponse cancelTicketById(CancelTicketRequest updateTicketRequest);

    TicketWithSeatsResponse bookTicket(BookingTicketRequest bookingTicketRequest);

    TicketWithSeatsResponse getTicketById(Integer ticketId, Integer customerId);
}
