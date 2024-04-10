package com.oop.ticketmasterswiftiebackend.event.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.event.constants.EventError;
import com.oop.ticketmasterswiftiebackend.event.constants.EventStatus;
import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupDetailEntity;
import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupEntity;
import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupPricingEntity;
import com.oop.ticketmasterswiftiebackend.event.models.request.CreateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventRequest;
import com.oop.ticketmasterswiftiebackend.event.models.request.UpdateEventStatusRequest;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.BaseEventGroupDetailsResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.FullEventGroupDetailsResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.*;
import com.oop.ticketmasterswiftiebackend.event.repository.EventGroupDetailRepository;
import com.oop.ticketmasterswiftiebackend.event.repository.EventGroupPricingRepository;
import com.oop.ticketmasterswiftiebackend.event.repository.EventGroupRepository;
import com.oop.ticketmasterswiftiebackend.event.service.EventService;
import com.oop.ticketmasterswiftiebackend.media.models.response.MediaUploadResponse;
import com.oop.ticketmasterswiftiebackend.media.service.MediaService;
import com.oop.ticketmasterswiftiebackend.ticket.service.TicketService;
import com.oop.ticketmasterswiftiebackend.transaction.service.StripeService;
import com.oop.ticketmasterswiftiebackend.venue.service.VenueService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventGroupRepository eventGroupRepository;
    private final EventGroupDetailRepository eventGroupDetailRepository;
    private final EventGroupPricingRepository eventGroupPricingRepository;
    private final MediaService mediaService;
    private final VenueService venueService;
    private final StripeService stripeService;
    private final TicketService ticketService;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional
    public CreateUpdateEventGroupResponse createEvent(CreateEventRequest request) {
        // Check if venue exist
        venueService.getVenueEntityById(request.getVenueId());

        EventGroupEntity newEvent = mapper.map(request, EventGroupEntity.class);
        newEvent.setStatus(EventStatus.UPCOMING);
        try {
            CreateUpdateEventGroupResponse eventResponse = mapper.map(eventGroupRepository.save(newEvent), CreateUpdateEventGroupResponse.class);
            // Create event group pricing
            createEventGroupPricing(eventResponse.getEventGroupId(), request.getCategoryPrices());
            // For each event group detail, create a new event group detail entity and save it
            List<BaseEventGroupDetailsResponse> eventGroupDetails = eventGroupDetailRepository.saveAll(
                    request.getTimings().stream().map(timing -> EventGroupDetailEntity.builder()
                            .eventGroupId(eventResponse.getEventGroupId())
                            .dateTime(timing)
                            .build()).toList()).stream().map(eventGroupDetailEntity -> {
                // Map back to Base Event Group Details Response
                BaseEventGroupDetailsResponse resp = mapper.map(eventGroupDetailEntity, BaseEventGroupDetailsResponse.class);
                // Create venue area for each event group detail
                venueService.createEventVenueArea(request.getVenueId(), eventGroupDetailEntity.getEventGroupDetailId(), request.getCategoryPrices());
                return resp;
            }).toList();
            eventResponse.setEventGroupDetails(eventGroupDetails);
            return eventResponse;
        } catch (Exception e) {
            log.error("Error occurred while creating event: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    private void createEventGroupPricing(Integer eventGroupId, Map<VenueAreaCategory, Double> categoryPrices) {
        List<EventGroupPricingEntity> eventGroupPricingEntities = new ArrayList<>();
        categoryPrices.forEach((category, price) -> eventGroupPricingEntities.add(EventGroupPricingEntity.builder()
                .eventGroupId(eventGroupId)
                .category(category)
                .price(price)
                .build()));
        eventGroupPricingRepository.saveAll(eventGroupPricingEntities);
    }


    @Override
    @Transactional
    public CreateUpdateEventGroupResponse updateEvent(UpdateEventRequest request) {
        EventGroupEntity eventGroupEntity = getEventEntityById(request.getEventGroupId());
        // Check if venue exist
        venueService.getVenueEntityById(request.getVenueId());

        // If event is open, prevent further changes
        if (Boolean.TRUE.equals(eventGroupEntity.getBookingAllowed())) {
            throw new BaseException(EventError.EVENT_GROUP_NOT_EDITABLE.getCode(), EventError.EVENT_GROUP_NOT_EDITABLE.getBusinessCode(), EventError.EVENT_GROUP_NOT_EDITABLE.getDescription());
        }
        // Map request to entity
        mapper.map(request, eventGroupEntity);
        try {
            // Delete the existing event group details and create new ones
            eventGroupDetailRepository.deleteByEventGroupId(request.getEventGroupId());
            // Update the event group details
            CreateUpdateEventGroupResponse eventResponse = mapper.map(eventGroupRepository.save(eventGroupEntity), CreateUpdateEventGroupResponse.class);
            // For each event group detail, create a new event group detail entity and save it
            List<BaseEventGroupDetailsResponse> eventGroupDetails = eventGroupDetailRepository.saveAll(
                    request.getTimings().stream().map(timing -> EventGroupDetailEntity.builder()
                            .eventGroupId(eventResponse.getEventGroupId())
                            .dateTime(timing)
                            .build()).toList()).stream().map(eventGroupDetailEntity -> {
                // Map back to Base Event Group Details Response
                BaseEventGroupDetailsResponse resp = mapper.map(eventGroupDetailEntity, BaseEventGroupDetailsResponse.class);
                // Create venue area for each event group detail
                venueService.createEventVenueArea(request.getVenueId(), eventGroupDetailEntity.getEventGroupDetailId(), request.getCategoryPrices());
                return resp;
            }).toList();
            eventResponse.setEventGroupDetails(eventGroupDetails);
            return eventResponse;
        } catch (Exception e) {
            log.error("Error occurred while creating event: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional
    public BaseEventGroupResponse updateEventImage(Integer eventGroupId, MultipartFile bannerImage, MultipartFile posterImage) {
        EventGroupEntity eventGroupEntity = getEventEntityById(eventGroupId);
        // Delete images
        List<String> existingImages = List.of(eventGroupEntity.getBannerImgUrl(), eventGroupEntity.getPosterImgUrl());
        mediaService.deleteMedia(existingImages);

        // Upload image
        MediaUploadResponse mediaUploadResponse = mediaService.uploadMedia(new MultipartFile[]{bannerImage, posterImage});
        eventGroupEntity.setBannerImgUrl(mediaUploadResponse.getFileNames().get(0));
        eventGroupEntity.setPosterImgUrl(mediaUploadResponse.getFileNames().get(1));

        try {
            // Add the new images
            return mapper.map(eventGroupRepository.save(eventGroupEntity), BaseEventGroupResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while updating event image: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventGroupResponse getFullEventGroupResponseById(Integer id) {
        return mapper.map(getEventEntityById(id), FullEventGroupResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventGroupWithVenueTimingGroupDetailsResponse> getAllEventGroups(Integer pageNo, String sortBy) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "eventGroupId";
        }
        Pageable pageable = PageRequest.of(pageNo, 15, Sort.by(sortBy));
        Page<EventGroupEntity> pagedResult = eventGroupRepository.findByIsDeletedFalse(pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.map(eventEntity -> {
                EventGroupWithVenueTimingGroupDetailsResponse mappedEventGroup = mapper.map(eventEntity, EventGroupWithVenueTimingGroupDetailsResponse.class);
                mappedEventGroup.setTiming(mappedEventGroup.retrieveAndAddTiming());
                return mappedEventGroup;
            }).getContent();
        }
        return new ArrayList<>();
    }

    @Override
    public String deleteEventGroup(Integer eventGroupId) {
        EventGroupEntity eventGroupEntity = getEventEntityById(eventGroupId);
        try {
            eventGroupEntity.setDeleted(true);
            eventGroupRepository.save(eventGroupEntity);
            return String.format("EventGroup with id of %s deleted successfully", eventGroupId);
        } catch (Exception e) {
            log.error("Error occurred while deleting event: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FullEventGroupDetailsResponse getEventGroupDetailsById(Integer eventGroupDetailId) {
        return mapper.map(eventGroupDetailRepository.findById(eventGroupDetailId).orElseThrow(() -> new BaseException(EventError.EVENT_GROUP_DETAIL_NOT_FOUND.getCode(), EventError.EVENT_GROUP_DETAIL_NOT_FOUND.getBusinessCode(), EventError.EVENT_GROUP_DETAIL_NOT_FOUND.getDescription())), FullEventGroupDetailsResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public EventGroupInformationResponse getEventGroupInformationById(Integer eventGroupDetail) {
        return mapper.map(getEventEntityById(eventGroupDetail), EventGroupInformationResponse.class);
    }

    @Override
    @Transactional
    public BaseEventGroupResponse updateEventStatus(UpdateEventStatusRequest request) {
        if (!updateEventStatusRuleChecker(request)) {
            throw new BaseException(EventError.EVENT_STATUS_UPDATE_RULE_VIOLATION.getCode(), EventError.EVENT_STATUS_UPDATE_RULE_VIOLATION.getBusinessCode(), EventError.EVENT_STATUS_UPDATE_RULE_VIOLATION.getDescription());
        }

        EventGroupEntity eventGroupEntity = getEventEntityById(request.getEventGroupId());
        eventGroupEntity.setBookingAllowed(request.getIsBookingAllowed());
        eventGroupEntity.setStatus(request.getStatus());
        try {
            return mapper.map(eventGroupRepository.save(eventGroupEntity), BaseEventGroupResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while updating event status: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    private boolean updateEventStatusRuleChecker(UpdateEventStatusRequest request) {
        // Booking open = Event must be open
        if (Boolean.TRUE.equals(request.getIsBookingAllowed()) && request.getStatus().equals(EventStatus.OPENED)) {
            return true;
        }
        // Booking closed = Event must be not open
        if (!Boolean.TRUE.equals(request.getIsBookingAllowed()) && !request.getStatus().equals(EventStatus.OPENED)) {
            return true;
        }
        return false;
    }

    @Override
    public EventGroupEntity getEventEntityById(Integer id) {
        return eventGroupRepository.findByEventGroupIdAndIsDeletedFalse(id).orElseThrow(() -> new BaseException(EventError.EVENT_GROUP_NOT_FOUND.getCode(), EventError.EVENT_GROUP_NOT_FOUND.getBusinessCode(), EventError.EVENT_GROUP_NOT_FOUND.getDescription()));
    }

    @Override
    @Transactional
    public void cancelEvent(Integer eventGroupId, Boolean processRefund) {
        EventGroupEntity eventGroupEntity = getEventEntityById(eventGroupId);
        checkIfEventCanBeCancelled(eventGroupEntity);
        eventGroupEntity.setStatus(EventStatus.CANCELED);

        try {
            // Cancel event
            eventGroupRepository.save(eventGroupEntity);
            List<Integer> eventGroupDetailIds = eventGroupEntity.getEventGroupDetails().stream().map(EventGroupDetailEntity::getEventGroupDetailId).toList();
            // Trigger refund and cancel tickets
            ticketService.cancelTicketsByEventGroupDetails(eventGroupDetailIds);
            if (Boolean.TRUE.equals(processRefund)) {
                stripeService.processEventGroupCancellationRefund(eventGroupDetailIds);
            }
        } catch (Exception e) {
            log.error("Error occurred while canceling event: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    private void checkIfEventCanBeCancelled(EventGroupEntity eventGroupEntity) {
        if (eventGroupEntity.getStatus().equals(EventStatus.CANCELED) || eventGroupEntity.getStatus().equals(EventStatus.PASSED)) {
            throw new BaseException(EventError.EVENT_ALREADY_CANCELED.getCode(), EventError.EVENT_ALREADY_CANCELED.getBusinessCode(), EventError.EVENT_ALREADY_CANCELED.getDescription());
        }
    }
}
