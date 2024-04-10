package com.oop.ticketmasterswiftiebackend.user.service.impl;

import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketStatus;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.BookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.UpdateTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.ticket.service.TicketService;
import com.oop.ticketmasterswiftiebackend.user.constants.AccountStatus;
import com.oop.ticketmasterswiftiebackend.user.constants.RoleName;
import com.oop.ticketmasterswiftiebackend.user.constants.UserError;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelTicketRequest;
import com.oop.ticketmasterswiftiebackend.user.service.CustomerService;
import com.oop.ticketmasterswiftiebackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private final UserService userService;
    private final TicketService ticketService;
    private final int CUSTOMER_ROLE_ID = 1;

    @Override
    @Transactional(readOnly = true)
    public List<TicketWithSeatsResponse> getTicketsByCustomerId(Integer customerId) {
        userService.getUserByIdAndStatus(customerId, AccountStatus.ACTIVE);
        return ticketService.getTicketsByCustomerId(customerId);
    }

    // TODO: Add in refund logic
    @Override
    @Transactional
    public BaseTicketResponse cancelTicketById(CancelTicketRequest cancelTicketRequest) {
        // If User != Customer id, throw exception - Only customer can cancel ticket
        if (!Objects.equals(cancelTicketRequest.getCustomerId(), cancelTicketRequest.getUserId())) {
            throw new BaseException(UserError.CUSTOMER_CANCEL_ERROR.getCode(), UserError.CUSTOMER_CANCEL_ERROR.getBusinessCode(), "Only customer can cancel ticket");
        }
        userService.getUserByIdAndStatus(cancelTicketRequest.getCustomerId(), AccountStatus.ACTIVE);
        UpdateTicketRequest updateTicketRequest = UpdateTicketRequest.builder()
                .userId(cancelTicketRequest.getCustomerId())
                .ticketId(cancelTicketRequest.getTicketId())
                .status(TicketStatus.CANCELLED_BY_CUSTOMER)
                .build();
        return ticketService.cancelTicketById(updateTicketRequest, RoleName.USER);
    }

    // TODO: Add in payment logic
    @Override
    public TicketWithSeatsResponse bookTicket(BookingTicketRequest bookingTicketRequest) {
        // 1. Check if customer exist
        userService.getUserByIdAndStatus(bookingTicketRequest.getCustomerId(), AccountStatus.ACTIVE);
        // 2. Proceed to save ticket
        return ticketService.createTicket(bookingTicketRequest, true);
    }

    @Override
    public TicketWithSeatsResponse getTicketById(Integer ticketId, Integer customerId) {
        userService.getUserByIdAndStatus(customerId, AccountStatus.ACTIVE);
        TicketWithSeatsResponse ticket = ticketService.getTicketById(ticketId);
        if (ticket.getCustomerId() != customerId) {
            log.error("Customer ID and Ticket Customer ID does not match, not authorised to access this ticket");
            throw new BaseException(UserError.CUSTOMER_TICKET_ERROR.getCode(), UserError.CUSTOMER_TICKET_ERROR.getBusinessCode(), "Customer does not have access to this ticket");
        }

        return ticket;
    }
}
