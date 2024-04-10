package com.oop.ticketmasterswiftiebackend.seat.repository;

import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import com.oop.ticketmasterswiftiebackend.seat.constants.SeatStatus;
import com.oop.ticketmasterswiftiebackend.seat.models.entities.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<SeatEntity, Integer> {

    List<SeatEntity> findByEventVenueAreaIdAndCategoryAndStatus(Integer eventVenueAreaId, VenueAreaCategory category, SeatStatus status);

    List<SeatEntity> findByEventVenueAreaIdAndCategory(Integer eventVenueAreaId, VenueAreaCategory category);

    List<SeatEntity> findByEventVenueAreaIdAndStatus(Integer eventVenueAreaId, SeatStatus status);

    List<SeatEntity> findByEventVenueAreaId(Integer eventVenueAreaId);

    List<SeatEntity> findBySeatIdIn(List<Integer> seatIds);
}
