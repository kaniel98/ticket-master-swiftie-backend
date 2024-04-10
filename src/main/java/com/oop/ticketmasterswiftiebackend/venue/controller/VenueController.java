package com.oop.ticketmasterswiftiebackend.venue.controller;

import com.oop.ticketmasterswiftiebackend.common.constants.APIConstants;
import com.oop.ticketmasterswiftiebackend.common.models.BaseResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.FullVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.VenueWithSeatMapResponse;
import com.oop.ticketmasterswiftiebackend.venue.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/venue")
public class VenueController {
    private final VenueService venueService;

    @GetMapping("/get")
    @Operation(summary = "Get Venue by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<FullVenueResponse>> getRoleById(@RequestParam Integer venueId) {
        return BaseResponse.successResponse(venueService.getVenueById(venueId));
    }

    @GetMapping("/all-venues")
    @Operation(summary = "Get all venues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All venues retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<List<VenueWithSeatMapResponse>>> getAllVenues() {
        return BaseResponse.successResponse(venueService.getAllVenues());
    }
}
