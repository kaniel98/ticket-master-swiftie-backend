package com.oop.ticketmasterswiftiebackend.user.service.impl;

import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.event.models.request.CreateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventStatusRequest;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.BaseEventGroupResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.CreateUpdateEventGroupResponse;
import com.oop.ticketmasterswiftiebackend.event.service.EventService;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.response.FullEventStatisticResponse;
import com.oop.ticketmasterswiftiebackend.eventStatistics.service.EventStatisticService;
import com.oop.ticketmasterswiftiebackend.user.constants.AccountStatus;
import com.oop.ticketmasterswiftiebackend.user.constants.UserError;
import com.oop.ticketmasterswiftiebackend.user.models.entities.UserEntity;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelEventRequest;
import com.oop.ticketmasterswiftiebackend.user.service.EventManagerService;
import com.oop.ticketmasterswiftiebackend.user.service.UserService;
import com.oop.ticketmasterswiftiebackend.venue.models.request.CreateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.PartialUpdateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.FullVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventManagerServiceImpl implements EventManagerService {
    private final VenueService venueService;
    private final EventService eventService;
    private final UserService userService;
    private final EventStatisticService eventStatisticService;
    private final Integer eventManagerRoleId = 2;

    @Override
    public BaseVenueResponse createVenue(CreateVenueRequest request) {
        return venueService.createVenue(request);
    }

    @Override
    public FullVenueResponse partialUpdateVenue(PartialUpdateVenueRequest request) {
        return venueService.partialVenueUpdate(request);
    }

    @Override
    public String deleteVenue(Integer venueId) {
        return venueService.deleteVenueById(venueId);
    }

    @Override
    public CreateUpdateEventGroupResponse createEventGroup(CreateEventRequest request) {
        // Check if manager exists and is active
        checkIfUserIsEventManagerAndIsActive(request.getEventManagerId());
        return eventService.createEvent(request);
    }

    @Override
    public CreateUpdateEventGroupResponse updateEventGroup(UpdateEventRequest request) {
        checkIfUserIsEventManagerAndIsActive(request.getEventManagerId());
        return eventService.updateEvent(request);
    }

    @Override
    public BaseEventGroupResponse uploadImageForEventGroup(Integer eventGroupId, MultipartFile bannerImage, MultipartFile posterImage) {
        return eventService.updateEventImage(eventGroupId, bannerImage, posterImage);
    }


    @Override
    public String deleteEventGroup(Integer eventGroupId) {
        return eventService.deleteEventGroup(eventGroupId);
    }

    @Override
    public BaseEventGroupResponse updateEventStatus(UpdateEventStatusRequest request) {
        return eventService.updateEventStatus(request);
    }

    @Override
    public String cancelEvent(CancelEventRequest request) {
        checkIfUserIsEventManagerAndIsActive(request.getEventManagerId());
        log.info("Event manager with id {} is cancelling event with id {}", request.getEventManagerId(), request.getEventGroupId());
        eventService.cancelEvent(request.getEventGroupId(), request.getProcessRefund());
        return "Event cancelled successfully";
    }

    @Override
    public FullEventStatisticResponse getEventStatistic(Integer eventGroupId, Integer eventManagerId) {
        return eventStatisticService.getEventStatistic(eventGroupId, eventManagerId);
    }

    @Override
    public FullEventStatisticResponse refreshEventStatistic(Integer eventGroupId, Integer eventManagerId) {
        return eventStatisticService.refreshEventStatistic(eventGroupId, null, eventManagerId);
    }

    private void checkIfUserIsEventManagerAndIsActive(Integer userId) {
        UserEntity manager = userService.getUserById(userId);
        if (manager.getStatus() != AccountStatus.ACTIVE && !Objects.equals(manager.getRoleId(), eventManagerRoleId)) {
            log.error("Event Manager with id {} not found or is not active", userId);
            throw new BaseException(UserError.USER_NOT_AUTHORIZED.getCode(), UserError.USER_NOT_AUTHORIZED.getBusinessCode(), UserError.USER_NOT_AUTHORIZED.getDescription());
        }
    }
}
