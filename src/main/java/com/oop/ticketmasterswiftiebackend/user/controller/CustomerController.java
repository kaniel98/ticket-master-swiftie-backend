package com.oop.ticketmasterswiftiebackend.user.controller;

import com.oop.ticketmasterswiftiebackend.common.constants.APIConstants;
import com.oop.ticketmasterswiftiebackend.common.models.BaseRequest;
import com.oop.ticketmasterswiftiebackend.common.models.BaseResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.BookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelTicketRequest;
import com.oop.ticketmasterswiftiebackend.user.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/get-all-tickets")
    @Operation(summary = "Get all tickets for a given customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<List<TicketWithSeatsResponse>>> getTicketsByCustomerId(
            @RequestParam Integer customerId) {
        return BaseResponse.successResponse(customerService.getTicketsByCustomerId(customerId));
    }

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
        return BaseResponse.successResponse(customerService.cancelTicketById(request.getData()));
    }

    @PostMapping("/book-ticket")
    @Operation(summary = "Booking ticket by customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket booked successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<TicketWithSeatsResponse>> customerBookingTicket(
            @RequestBody @Valid BaseRequest<BookingTicketRequest> request) {
        return BaseResponse.createdResponse(customerService.bookTicket(request.getData()));
    }

    @GetMapping("/get-ticket")
    @Operation(summary = "Get ticket by ticketId (id)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<TicketWithSeatsResponse>> getTicketById(
            @RequestParam Integer ticketId, @RequestParam Integer customerId) {
        return BaseResponse.successResponse(customerService.getTicketById(ticketId, customerId));
    }
}
