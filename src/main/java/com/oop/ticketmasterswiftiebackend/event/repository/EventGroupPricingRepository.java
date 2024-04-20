package com.oop.ticketmasterswiftiebackend.event.repository;

import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupPricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EventGroupPricingRepository extends JpaRepository<EventGroupPricingEntity, Integer> {
    @Transactional
    void deleteByEventGroupId(Integer eventGroupId);
}
