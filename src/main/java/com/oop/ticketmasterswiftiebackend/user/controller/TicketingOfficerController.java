package com.oop.ticketmasterswiftiebackend.user.controller;

import com.oop.ticketmasterswiftiebackend.common.constants.APIConstants;
import com.oop.ticketmasterswiftiebackend.common.models.BaseRequest;
import com.oop.ticketmasterswiftiebackend.common.models.BaseResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.TicketingOfficerBookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelTicketRequest;
import com.oop.ticketmasterswiftiebackend.user.models.request.RedeemTicketRequest;
import com.oop.ticketmasterswiftiebackend.user.models.request.VerifyTicketValidityRequest;
import com.oop.ticketmasterswiftiebackend.user.service.TicketingOfficerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticketing-officer")
@RequiredArgsConstructor
public class TicketingOfficerController {
    private final TicketingOfficerService ticketingOfficerService;

    @PatchMapping("/cancel-ticket")
    @Operation(summary = "Cancel ticket by ticketId (id)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket cancelled successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<BaseTicketResponse>> cancelTicketById(
            @RequestBody @Valid BaseRequest<CancelTicketRequest> request) {
        return BaseResponse.successResponse(ticketingOfficerService.cancelTicketById(request.getData()));
    }

    @PostMapping("/book-ticket")
    @Operation(summary = "Booking ticket by ticketing officer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket booked successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<TicketWithSeatsResponse>> ticketOfficerBookingTicket(
            @RequestBody @Valid BaseRequest<TicketingOfficerBookingTicketRequest> request) {
        return BaseResponse.createdResponse(ticketingOfficerService.createTicket(request.getData()));
    }

    @PostMapping("/verify-ticket")
    @Operation(summary = "Verify the validity of a ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket verified successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<TicketWithSeatsResponse>> verifyTicketValidity(
            @RequestBody @Valid BaseRequest<VerifyTicketValidityRequest> request) {
        return BaseResponse.successResponse(ticketingOfficerService.verifyTicketValidity(request.getData()));
    }

    @PatchMapping("/redeem-ticket")
    @Operation(summary = "Redeem a given ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket redeemed successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<BaseTicketResponse>> redeemTicket(
            @RequestBody @Valid BaseRequest<RedeemTicketRequest> request) {
        return BaseResponse.successResponse(ticketingOfficerService.redeemTicket(request.getData()));
    }
}
