package com.oop.ticketmasterswiftiebackend.eventStatistics.repository;

import com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.EventCategoryStatisticEntity;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.key.EventCategoryStatisticKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventCategoryStatisticRepository extends JpaRepository<EventCategoryStatisticEntity, EventCategoryStatisticKey> {
    List<EventCategoryStatisticEntity> findByIdEventStatisticId(Integer eventStatisticId);
}
