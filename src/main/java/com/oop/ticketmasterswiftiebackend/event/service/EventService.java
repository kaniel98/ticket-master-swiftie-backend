package com.oop.ticketmasterswiftiebackend.event.service;

import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupEntity;
import com.oop.ticketmasterswiftiebackend.event.models.request.CreateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventStatusRequest;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.FullEventGroupDetailsResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    CreateUpdateEventGroupResponse createEvent(CreateEventRequest request);

    CreateUpdateEventGroupResponse updateEvent(UpdateEventRequest request);

    BaseEventGroupResponse updateEventImage(Integer eventGroupId, MultipartFile bannerImage, MultipartFile posterImage);

    FullEventGroupResponse getFullEventGroupResponseById(Integer id);

    List<EventGroupWithVenueTimingGroupDetailsResponse> getAllEventGroups(Integer pageNo, String sortBy);

    String deleteEventGroup(Integer eventGroupId);

    FullEventGroupDetailsResponse getEventGroupDetailsById(Integer eventGroupDetailId);

    EventGroupInformationResponse getEventGroupInformationById(Integer eventGroupDetailId);

    BaseEventGroupResponse updateEventStatus(UpdateEventStatusRequest request);

    EventGroupEntity getEventEntityById(Integer id);

    void cancelEvent(Integer eventGroupId, Boolean processRefund);

}
