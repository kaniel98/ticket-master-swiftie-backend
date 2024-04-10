package com.oop.ticketmasterswiftiebackend.venue.repository;

import com.oop.ticketmasterswiftiebackend.venue.models.entities.EventVenueAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventVenueAreaRepository extends JpaRepository<EventVenueAreaEntity, Integer> {
    List<EventVenueAreaEntity> findByVenueIdAndEventGroupDetailId(Integer venueId, Integer eventId);

}
