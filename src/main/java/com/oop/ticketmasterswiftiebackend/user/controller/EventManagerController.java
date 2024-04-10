package com.oop.ticketmasterswiftiebackend.user.controller;

import com.oop.ticketmasterswiftiebackend.common.constants.APIConstants;
import com.oop.ticketmasterswiftiebackend.common.models.BaseRequest;
import com.oop.ticketmasterswiftiebackend.common.models.BaseResponse;
import com.oop.ticketmasterswiftiebackend.event.models.request.CreateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventStatusRequest;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.BaseEventGroupResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.CreateUpdateEventGroupResponse;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.response.FullEventStatisticResponse;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelEventRequest;
import com.oop.ticketmasterswiftiebackend.user.service.EventManagerService;
import com.oop.ticketmasterswiftiebackend.venue.models.request.CreateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.PartialUpdateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.FullVenueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-manager")
public class EventManagerController {
    private final EventManagerService eventManagerService;

    @PostMapping("/create-venue")
    @Operation(summary = "Create a new venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue created successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<BaseVenueResponse>> createVenue(@RequestBody @Valid BaseRequest<CreateVenueRequest> request) {
        return BaseResponse.successResponse(eventManagerService.createVenue(request.getData()));
    }

    @PatchMapping("/update-venue")
    @Operation(summary = "Partial update venue details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue details updated successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<FullVenueResponse>> updateVenue(@RequestBody @Valid BaseRequest<PartialUpdateVenueRequest> request) {
        return BaseResponse.successResponse(eventManagerService.partialUpdateVenue(request.getData()));
    }

    @DeleteMapping("/delete-venue")
    @Operation(summary = "Delete venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue deleted successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<String>> deleteVenue(@RequestParam Integer venueId) {
        return BaseResponse.successResponse(eventManagerService.deleteVenue(venueId));
    }

    @PostMapping("/create-event")
    @Operation(summary = "Create new event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New event created successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<CreateUpdateEventGroupResponse>> createEvent(@RequestBody @Valid BaseRequest<CreateEventRequest> request) {
        return BaseResponse.successResponse(eventManagerService.createEventGroup(request.getData()));
    }

    @PatchMapping("/update-event")
    @Operation(summary = "Update event details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<CreateUpdateEventGroupResponse>> updateEvent(@RequestBody @Valid BaseRequest<UpdateEventRequest> request) {
        return BaseResponse.successResponse(eventManagerService.updateEventGroup(request.getData()));
    }

    @PatchMapping("/upload-event-image")
    @Operation(summary = "Upload image for event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<BaseEventGroupResponse>> uploadImageForEvent(@RequestParam Integer eventGroupId, @RequestParam MultipartFile bannerImage, @RequestParam MultipartFile posterImage) {
        return BaseResponse.successResponse(eventManagerService.uploadImageForEventGroup(eventGroupId, bannerImage, posterImage));
    }

    @DeleteMapping("/delete-event-group")
    @Operation(summary = "Delete event group by event group id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event group deleted successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<String>> deleteEvent(@RequestParam Integer eventGroupId) {
        return BaseResponse.successResponse(eventManagerService.deleteEventGroup(eventGroupId));
    }

    @PatchMapping("/update-event-status")
    @Operation(summary = "Update event status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event status updated successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<BaseEventGroupResponse>> updateEventStatus(@RequestBody @Valid BaseRequest<UpdateEventStatusRequest> request) {
        return BaseResponse.successResponse(eventManagerService.updateEventStatus(request.getData()));
    }

    @GetMapping("/event-statistic")
    @Operation(summary = "Get event statistic by event group id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event statistic retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<FullEventStatisticResponse>> getEventStatistic(@RequestParam Integer eventGroupId, @RequestParam Integer eventManagerId) {
        return BaseResponse.successResponse(eventManagerService.getEventStatistic(eventGroupId, eventManagerId));
    }

    @GetMapping("/refresh-event-statistic")
    @Operation(summary = "Refresh event statistic by event group id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event statistic refreshed successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<FullEventStatisticResponse>> refreshEventStatistic(@RequestParam Integer eventGroupId, @RequestParam Integer eventManagerId) {
        return BaseResponse.successResponse(eventManagerService.refreshEventStatistic(eventGroupId, eventManagerId));
    }

    @PostMapping("/cancel-event")
    @Operation(summary = "Cancel event by event group id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event canceled successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public ResponseEntity<BaseResponse<String>> cancelEvent(@RequestBody @Valid BaseRequest<CancelEventRequest> request) {
        return BaseResponse.successResponse(eventManagerService.cancelEvent(request.getData()));
    }
}
