package com.oop.ticketmasterswiftiebackend.user.service;

import com.oop.ticketmasterswiftiebackend.event.models.request.CreateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventStatusRequest;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.BaseEventGroupResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.CreateUpdateEventGroupResponse;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.response.FullEventStatisticResponse;
import com.oop.ticketmasterswiftiebackend.user.models.request.CancelEventRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.CreateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.PartialUpdateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.FullVenueResponse;
import org.springframework.web.multipart.MultipartFile;

public interface EventManagerService {
    // Region: Venue related operations
    BaseVenueResponse createVenue(CreateVenueRequest request);

    FullVenueResponse partialUpdateVenue(PartialUpdateVenueRequest request);

    String deleteVenue(Integer venueId);
    // End Region

    // Region: Event related operations
    CreateUpdateEventGroupResponse createEventGroup(CreateEventRequest request);

    CreateUpdateEventGroupResponse updateEventGroup(UpdateEventRequest request);

    BaseEventGroupResponse uploadImageForEventGroup(Integer eventGroupId, MultipartFile bannerImage, MultipartFile posterImage);

    String deleteEventGroup(Integer eventGroupId);

    BaseEventGroupResponse updateEventStatus(UpdateEventStatusRequest request);

    String cancelEvent(CancelEventRequest request);
    // End Region

    // Region: Event statistics related operations
    FullEventStatisticResponse getEventStatistic(Integer eventGroupId, Integer eventManagerId);

    FullEventStatisticResponse refreshEventStatistic(Integer eventGroupId, Integer eventManagerId);
    // End Region

}
