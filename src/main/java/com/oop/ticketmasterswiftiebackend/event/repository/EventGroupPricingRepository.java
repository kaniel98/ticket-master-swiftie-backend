package com.oop.ticketmasterswiftiebackend.event.repository;

import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupPricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventGroupPricingRepository extends JpaRepository<EventGroupPricingEntity, Integer> {
}
