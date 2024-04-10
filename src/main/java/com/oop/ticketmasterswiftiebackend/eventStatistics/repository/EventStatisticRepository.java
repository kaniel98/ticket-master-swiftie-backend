package com.oop.ticketmasterswiftiebackend.eventStatistics.repository;

import com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.EventStatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventStatisticRepository extends JpaRepository<EventStatisticEntity, Integer> {
    Optional<EventStatisticEntity> findByEventGroupId(Integer eventGroupId);
}
