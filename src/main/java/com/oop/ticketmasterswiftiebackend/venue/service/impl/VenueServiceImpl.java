package com.oop.ticketmasterswiftiebackend.venue.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.event.constants.EventError;
import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import com.oop.ticketmasterswiftiebackend.venue.models.entities.EventVenueAreaEntity;
import com.oop.ticketmasterswiftiebackend.venue.models.entities.VenueAreaEntity;
import com.oop.ticketmasterswiftiebackend.venue.models.entities.VenueEntity;
import com.oop.ticketmasterswiftiebackend.venue.models.request.CreateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.PartialUpdateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.UpdateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.VenueAreaRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseEventVenueAreaResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.FullVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.VenueWithSeatMapResponse;
import com.oop.ticketmasterswiftiebackend.venue.repository.EventVenueAreaRepository;
import com.oop.ticketmasterswiftiebackend.venue.repository.VenueAreaRepository;
import com.oop.ticketmasterswiftiebackend.venue.repository.VenueRepository;
import com.oop.ticketmasterswiftiebackend.venue.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final VenueAreaRepository venueAreaRepository;
    private final EventVenueAreaRepository eventVenueAreaRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public VenueEntity getVenueEntityById(Integer id) {
        return venueRepository.findByVenueIdAndIsDeletedFalse(id).orElseThrow(
                () -> new BaseException(EventError.VENUE_NOT_FOUND.getCode(), EventError.VENUE_NOT_FOUND.getBusinessCode(), EventError.VENUE_NOT_FOUND.getDescription())
        );
    }

    // Note: Venue service will create the venue and the venue area
    // * Subsequent events using the venue will be using the venue area.
    @Override
    @Transactional
    public BaseVenueResponse createVenue(CreateVenueRequest request) {
        try {
            // Save venue
            VenueEntity savedVenue = venueRepository.save(VenueEntity.builder()
                    .name(request.getName())
                    .address(request.getAddress())
                    .isDeleted(false)
                    .build());
            // Prepare list of venue area entities
            List<VenueAreaEntity> venueAreasToBeSaved = new ArrayList<>();
            for (VenueAreaRequest venueAreaRequest : request.getSeatMap()) {
                for (int i = 0; i < venueAreaRequest.getNoOfCategory(); i++) {
                    venueAreasToBeSaved.add(VenueAreaEntity.builder()
                            .venueId(savedVenue.getVenueId())
                            .category(venueAreaRequest.getCategory())
                            .noOfCol(venueAreaRequest.getNoOfCol())
                            .noOfRow(venueAreaRequest.getNoOfRow())
                            .noOfSeat(venueAreaRequest.getNoOfRow() * venueAreaRequest.getNoOfCol())
                            .position(venueAreaRequest.getPositions()[i])
                            .isDeleted(false)
                            .build());
                }
            }
            venueAreaRepository.saveAll(venueAreasToBeSaved);
            return modelMapper.map(savedVenue, BaseVenueResponse.class);
        } catch (DataIntegrityViolationException e) {
            log.error("DataIntegrityViolationException occurred while creating venue: {}", e.getMessage());
            throw new BaseException(EventError.DUPLICATE_VENUE.getCode(), EventError.DUPLICATE_VENUE.getBusinessCode(), EventError.DUPLICATE_VENUE.getDescription());
        } catch (Exception e) {
            log.error("Error occurred while creating venue: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FullVenueResponse getVenueById(Integer id) {
        return modelMapper.map(getVenueEntityById(id), FullVenueResponse.class);
    }

    @Override
    @Transactional
    public FullVenueResponse updateVenue(UpdateVenueRequest request) {
        VenueEntity venue = getVenueEntityById(request.getVenueId());
        // Set the venue fields to be updated
        venue.setName(request.getName());
        venue.setAddress(request.getAddress());
        try {
            // Delete the venue areas with the given id
            venueAreaRepository.softDeleteByVenueId(request.getVenueId());
            // Save the new venue areas
            List<VenueAreaEntity> venueAreasToBeSaved = new ArrayList<>();
            for (VenueAreaRequest venueAreaRequest : request.getSeatMap()) {
                for (int i = 0; i < venueAreaRequest.getNoOfCategory(); i++) {
                    venueAreasToBeSaved.add(VenueAreaEntity.builder()
                            .venueId(venue.getVenueId())
                            .category(venueAreaRequest.getCategory())
                            .noOfCol(venueAreaRequest.getNoOfCol())
                            .noOfRow(venueAreaRequest.getNoOfRow())
                            .noOfSeat(venueAreaRequest.getNoOfRow() * venueAreaRequest.getNoOfCol())
                            .position(venueAreaRequest.getPositions()[i])
                            .isDeleted(false)
                            .build());
                }
            }
            venueAreaRepository.saveAll(venueAreasToBeSaved);
            // Save the venue
            return modelMapper.map(venueRepository.save(venue), FullVenueResponse.class);
        } catch (DataIntegrityViolationException e) {
            log.error("DataIntegrityViolationException occurred while updating venue: {}", e.getMessage());
            throw new BaseException(EventError.DUPLICATE_VENUE.getCode(), EventError.DUPLICATE_VENUE.getBusinessCode(), EventError.DUPLICATE_VENUE.getDescription());
        } catch (Exception e) {
            log.error("Error occurred while updating venue: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    public FullVenueResponse partialVenueUpdate(PartialUpdateVenueRequest request) {
        VenueEntity venue = getVenueEntityById(request.getVenueId());
        venue.setName(request.getName());
        venue.setAddress(request.getAddress());
        try {
            return modelMapper.map(venueRepository.save(venue), FullVenueResponse.class);
        } catch (DataIntegrityViolationException e) {
            log.error("DataIntegrityViolationException occurred while partial updating venue: {}", e.getMessage());
            throw new BaseException(EventError.DUPLICATE_VENUE.getCode(), EventError.DUPLICATE_VENUE.getBusinessCode(), EventError.DUPLICATE_VENUE.getDescription());
        } catch (Exception e) {
            log.error("Error occurred while partial updating venue: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueWithSeatMapResponse> getAllVenues() {
        return venueRepository.findAllByIsDeletedFalseOrderByNameAsc().stream().map(venueEntity -> modelMapper.map(venueEntity, VenueWithSeatMapResponse.class)).toList();
    }

    @Override
    public String deleteVenueById(Integer venueId) {
        VenueEntity venue = getVenueEntityById(venueId);
        venue.setIsDeleted(true);
        try {
            venueRepository.save(venue);
            return String.format("Venue with id of %s deleted successfully", venueId);
        } catch (Exception e) {
            log.error("Error occurred while deleting venue: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional
    @Async
    public Future<List<BaseEventVenueAreaResponse>> createEventVenueArea(Integer venueId, Integer eventGroupDetailId, Map<VenueAreaCategory, Double> priceMap) {
        // Get the venue areas for a given venue
        List<VenueAreaEntity> venueAreas = venueAreaRepository.findByVenueIdAndIsDeletedFalse(venueId);
        if (venueAreas.isEmpty()) {
            throw new BaseException(EventError.VENUE_AREA_NOT_FOUND.getCode(), EventError.VENUE_AREA_NOT_FOUND.getBusinessCode(), EventError.VENUE_AREA_NOT_FOUND.getDescription());
        }

        // Save the venue areas for the event
        List<EventVenueAreaEntity> eventVenueAreas = venueAreas.stream().map(area ->
                // Set the price for the given category
                EventVenueAreaEntity.builder()
                        .eventGroupDetailId(eventGroupDetailId)
                        .venueId(venueId)
                        .category(area.getCategory())
                        .noOfRow(area.getNoOfRow())
                        .noOfCol(area.getNoOfCol())
                        .noOfSeat(area.getNoOfSeat())
                        .price(priceMap.getOrDefault(area.getCategory(), 0.0))
                        .position(area.getPosition())
                        .build()
        ).toList();
        try {
            CompletableFuture<List<BaseEventVenueAreaResponse>> resp = new CompletableFuture<>();
            resp.complete(eventVenueAreaRepository.saveAll(eventVenueAreas).stream().map(eventVenueArea -> modelMapper.map(eventVenueArea, BaseEventVenueAreaResponse.class)).toList());
            return resp;
        } catch (Exception e) {
            log.error("Error occurred while creating event venue area: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BaseEventVenueAreaResponse> getEventVenueArea(Integer venueId, Integer eventGroupDetailId) {
        List<EventVenueAreaEntity> eventArea = eventVenueAreaRepository.findByVenueIdAndEventGroupDetailId(venueId, eventGroupDetailId);
        if (eventArea.isEmpty()) {
            throw new BaseException(EventError.VENUE_AREA_NOT_FOUND.getCode(), EventError.VENUE_AREA_NOT_FOUND.getBusinessCode(), EventError.VENUE_AREA_NOT_FOUND.getDescription());
        }
        return eventArea.stream().map(eventVenueArea -> modelMapper.map(eventVenueArea, BaseEventVenueAreaResponse.class)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventVenueAreaEntity getEventVenueAreaDetails(Integer eventVenueAreaId) {
        return eventVenueAreaRepository.findById(eventVenueAreaId).orElseThrow(
                () -> new BaseException(EventError.VENUE_AREA_NOT_FOUND.getCode(), EventError.VENUE_AREA_NOT_FOUND.getBusinessCode(), EventError.VENUE_AREA_NOT_FOUND.getDescription())
        );
    }

    @Override
    @Transactional
    public BaseEventVenueAreaResponse updateEventVenueArea(EventVenueAreaEntity updatedEventVenueArea) {
        try {
            return modelMapper.map(eventVenueAreaRepository.save(modelMapper.map(updatedEventVenueArea, EventVenueAreaEntity.class)), BaseEventVenueAreaResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while updating event venue area: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }
}
