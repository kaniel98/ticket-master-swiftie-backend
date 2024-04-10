package com.oop.ticketmasterswiftiebackend.venue.service;

import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import com.oop.ticketmasterswiftiebackend.venue.models.entities.EventVenueAreaEntity;
import com.oop.ticketmasterswiftiebackend.venue.models.entities.VenueEntity;
import com.oop.ticketmasterswiftiebackend.venue.models.request.CreateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.PartialUpdateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.request.UpdateVenueRequest;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseEventVenueAreaResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.FullVenueResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.VenueWithSeatMapResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface VenueService {
    BaseVenueResponse createVenue(CreateVenueRequest request);

    VenueEntity getVenueEntityById(Integer id);

    FullVenueResponse getVenueById(Integer id);

    FullVenueResponse updateVenue(UpdateVenueRequest request);

    FullVenueResponse partialVenueUpdate(PartialUpdateVenueRequest request);

    List<VenueWithSeatMapResponse> getAllVenues();

    String deleteVenueById(Integer id);

    Future<List<BaseEventVenueAreaResponse>> createEventVenueArea(Integer venueId, Integer eventId, Map<VenueAreaCategory, Double> priceMap);

    List<BaseEventVenueAreaResponse> getEventVenueArea(Integer venueId, Integer eventId);

    EventVenueAreaEntity getEventVenueAreaDetails(Integer eventVenueAreaId);

    BaseEventVenueAreaResponse updateEventVenueArea(EventVenueAreaEntity updatedEventVenueArea);
}
