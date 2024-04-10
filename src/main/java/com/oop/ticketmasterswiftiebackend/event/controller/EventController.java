package com.oop.ticketmasterswiftiebackend.event.controller;

import com.oop.ticketmasterswiftiebackend.common.constants.APIConstants;
import com.oop.ticketmasterswiftiebackend.common.models.BaseResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.FullEventGroupDetailsResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.EventGroupInformationResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.EventGroupWithVenueTimingGroupDetailsResponse;
import com.oop.ticketmasterswiftiebackend.event.service.EventService;
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
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/get")
    @Operation(summary = "Get Event Group by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<EventGroupInformationResponse>> getEventById(@RequestParam Integer eventGroupId) {
        return BaseResponse.successResponse(eventService.getEventGroupInformationById(eventGroupId));
    }

    @GetMapping("/all-events")
    @Operation(summary = "Get all events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<List<EventGroupWithVenueTimingGroupDetailsResponse>>> getAllEvents(@RequestParam Integer pageNo, @RequestParam String sortBy) {
        return BaseResponse.successResponse(eventService.getAllEventGroups(pageNo, sortBy));
    }

    @GetMapping("/get-event-group-details")
    @Operation(summary = "Get Event Group Details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event Group Details retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<FullEventGroupDetailsResponse>> getEventGroupDetail(@RequestParam Integer eventGroupDetailId) {
        return BaseResponse.successResponse(eventService.getEventGroupDetailsById(eventGroupDetailId));
    }
}
