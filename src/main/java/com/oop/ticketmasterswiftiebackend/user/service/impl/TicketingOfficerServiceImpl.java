package com.oop.ticketmasterswiftiebackend.user.service.impl;

import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketBookingMethod;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketStatus;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.BookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.TicketingOfficerBookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.UpdateTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.ticket.service.TicketService;
import com.oop.ticketmasterswiftiebackend.user.constants.AccountStatus;
import com.oop.ticketmasterswiftiebackend.user.constants.RoleName;
import com.oop.ticketmasterswiftiebackend.user.constants.UserError;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelTicketRequest;
import com.oop.ticketmasterswiftiebackend.user.models.request.RedeemTicketRequest;
import com.oop.ticketmasterswiftiebackend.user.models.request.VerifyTicketValidityRequest;
import com.oop.ticketmasterswiftiebackend.user.models.response.UserResponse;
import com.oop.ticketmasterswiftiebackend.user.service.TicketingOfficerService;
import com.oop.ticketmasterswiftiebackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class TicketingOfficerServiceImpl implements TicketingOfficerService {
    private final UserService userService;
    private final TicketService ticketService;
    private final int TICKETING_OFFICER_ROLE_ID = 3;
    private final String SUBSTITUTION_EMAIL = "NO_ACCOUNT";

    @Override
    public BaseTicketResponse cancelTicketById(CancelTicketRequest cancelTicketRequest) {
        isTicketingOfficer(cancelTicketRequest.getUserId());
        UpdateTicketRequest request = UpdateTicketRequest.builder()
                .userId(cancelTicketRequest.getCustomerId())
                .ticketId(cancelTicketRequest.getTicketId())
                .status(TicketStatus.CANCELLED_BY_ORGANISER)
                .build();
        return ticketService.cancelTicketById(request, RoleName.TICKETING_OFFICER);
    }

    @Override
    public TicketWithSeatsResponse createTicket(TicketingOfficerBookingTicketRequest ticketingOfficerBookingTicketRequest) {
        // Check if the booker is a ticketing officer
        isTicketingOfficer(ticketingOfficerBookingTicketRequest.getUserId());
        // Check if the customer email has an account
        UserResponse customerAccount;
        try {
            customerAccount = userService.getUserByEmail(ticketingOfficerBookingTicketRequest.getCustomerEmail());
        } catch (BaseException e) {
            customerAccount = userService.getUserByEmail(SUBSTITUTION_EMAIL);
        }
        // TODO - Get payment flow from the payment service
        BookingTicketRequest bookingTicketRequest = BookingTicketRequest.builder()
                .customerId(customerAccount.getUserId())
                .eventGroupDetailId(ticketingOfficerBookingTicketRequest.getEventGroupDetailId())
                .eventVenueAreaId(ticketingOfficerBookingTicketRequest.getEventVenueAreaId())
                .bookingMethod(TicketBookingMethod.ONSITE) // Assuming ticket officer issue on site
                .numberOfGuests(ticketingOfficerBookingTicketRequest.getNumberOfGuests())
                .status(TicketStatus.ACTIVE)
                .build();
        return ticketService.createTicket(bookingTicketRequest, true);
    }

    private void isTicketingOfficer(Integer userId) {
        // Check if the booker is a ticketing officer
        try {
            userService.getUseByIdAndStatusAndRoleId(userId, AccountStatus.ACTIVE, TICKETING_OFFICER_ROLE_ID);
        } catch (BaseException e) {
            log.error("User is not a ticketing officer");
            throw new BaseException(UserError.USER_NOT_AUTHORIZED.getCode(), UserError.USER_NOT_AUTHORIZED.getBusinessCode(), UserError.USER_NOT_AUTHORIZED.getDescription());
        }
    }

    @Override
    public TicketWithSeatsResponse verifyTicketValidity(VerifyTicketValidityRequest request) {
        return ticketService.verifyTicketValidity(request);
    }

    @Override
    public BaseTicketResponse redeemTicket(RedeemTicketRequest request) {
        isTicketingOfficer(request.getTicketOfficerId());
        return ticketService.redeemTicket(UpdateTicketRequest.builder()
                .userId(request.getTicketOfficerId())
                .ticketId(request.getTicketId())
                .status(TicketStatus.REDEEMED)
                .build());
    }
}
