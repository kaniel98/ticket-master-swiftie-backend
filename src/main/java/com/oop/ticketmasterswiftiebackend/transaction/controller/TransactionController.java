package com.oop.ticketmasterswiftiebackend.transaction.controller;

import com.oop.ticketmasterswiftiebackend.common.constants.APIConstants;
import com.oop.ticketmasterswiftiebackend.common.models.BaseRequest;
import com.oop.ticketmasterswiftiebackend.common.models.BaseResponse;
import com.oop.ticketmasterswiftiebackend.transaction.models.request.CreateTransactionRequest;
import com.oop.ticketmasterswiftiebackend.transaction.models.response.TransactionResponse;
import com.oop.ticketmasterswiftiebackend.transaction.service.TransactionService;
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
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create transaction", description = "Create transaction for ticket purchase/refund")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction recorded successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<TransactionResponse>> createTransaction(
            @RequestBody @Valid BaseRequest<CreateTransactionRequest> request) {
        return BaseResponse.successResponse(transactionService.createTransaction(request.getData()));
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get transaction by transaction id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<TransactionResponse>> getTransactionById(
            @PathVariable Integer transactionId) {
        return BaseResponse.successResponse(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/{ticketId}")
    @Operation(summary = "Get transaction by ticket id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<List<TransactionResponse>>> getTransactionByTicketId(
            @PathVariable Integer ticketId) {
        return BaseResponse.successResponse(transactionService.getTransactionByTicketId(ticketId));
    }
}
