package com.oop.ticketmasterswiftiebackend.venue.repository;

import com.oop.ticketmasterswiftiebackend.venue.models.entities.VenueAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenueAreaRepository extends JpaRepository<VenueAreaEntity, Integer> {
    @Modifying
    @Query("UPDATE VenueAreaEntity vae SET vae.isDeleted = true WHERE vae.venueId = :venueId")
    void softDeleteByVenueId(Integer venueId);

    List<VenueAreaEntity> findByVenueIdAndIsDeletedFalse(Integer venueId);
}
