package com.oop.ticketmasterswiftiebackend.user.service;

import com.oop.ticketmasterswiftiebackend.ticket.models.request.TicketingOfficerBookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelTicketRequest;
import com.oop.ticketmasterswiftiebackend.user.models.request.RedeemTicketRequest;
import com.oop.ticketmasterswiftiebackend.user.models.request.VerifyTicketValidityRequest;

public interface TicketingOfficerService {
    BaseTicketResponse cancelTicketById(CancelTicketRequest updateTicketRequest);

    TicketWithSeatsResponse createTicket(TicketingOfficerBookingTicketRequest bookingTicketRequest);

    TicketWithSeatsResponse verifyTicketValidity(VerifyTicketValidityRequest verifyTicketValidityRequest);

    BaseTicketResponse redeemTicket(RedeemTicketRequest redeemTicketRequest);
}
