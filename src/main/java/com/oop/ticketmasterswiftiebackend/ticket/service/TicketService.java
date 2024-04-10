package com.oop.ticketmasterswiftiebackend.ticket.service;

import com.oop.ticketmasterswiftiebackend.ticket.models.entities.TicketEntity;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.BookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.UpdateTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.FullTicketSeatResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.user.constants.RoleName;
import com.oop.ticketmasterswiftiebackend.user.models.request.VerifyTicketValidityRequest;

import java.util.List;

public interface TicketService {

    TicketWithSeatsResponse getTicketById(Integer ticketId);

    TicketWithSeatsResponse createTicket(BookingTicketRequest ticketBookingRequest, Boolean sendEmail);

    List<TicketWithSeatsResponse> getTicketsByCustomerId(Integer customerId);

    BaseTicketResponse cancelTicketById(UpdateTicketRequest updateTicketRequest, RoleName cancelledBy);

    void cancelTicketsByEventGroupDetails(List<Integer> eventGroupDetailsIds);

    BaseTicketResponse updateTicketQrCodeImage(String qrCodeImageUrl, Integer ticketId);

    TicketWithSeatsResponse verifyTicketValidity(VerifyTicketValidityRequest verifyTicketValidityRequest);

    BaseTicketResponse redeemTicket(UpdateTicketRequest updateTicketRequest);

    List<FullTicketSeatResponse> getAllValidTicketsSoldForEventGroupDetail(Integer eventGroupDetailId);

    List<Integer> listOfTicketIdForEventGroupDetails(List<Integer> eventGroupDetailsId);

    TicketEntity getTicketEntityById(Integer ticketId);
}