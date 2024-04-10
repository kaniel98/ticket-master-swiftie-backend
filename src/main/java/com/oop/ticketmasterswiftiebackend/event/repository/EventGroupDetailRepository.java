package com.oop.ticketmasterswiftiebackend.event.repository;

import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EventGroupDetailRepository extends JpaRepository<EventGroupDetailEntity, Integer> {
    @Transactional
    void deleteByEventGroupId(Integer eventGroupId);
}
