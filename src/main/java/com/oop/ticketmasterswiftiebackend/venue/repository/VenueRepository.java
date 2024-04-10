package com.oop.ticketmasterswiftiebackend.venue.repository;

import com.oop.ticketmasterswiftiebackend.venue.models.entities.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Integer> {
    List<VenueEntity> findAllByIsDeletedFalseOrderByNameAsc();

    Optional<VenueEntity> findByVenueIdAndIsDeletedFalse(Integer id);
}
