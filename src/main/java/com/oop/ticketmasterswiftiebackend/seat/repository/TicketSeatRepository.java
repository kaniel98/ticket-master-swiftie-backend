package com.oop.ticketmasterswiftiebackend.seat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oop.ticketmasterswiftiebackend.seat.models.entities.TicketSeatEntity;

public interface TicketSeatRepository extends JpaRepository<TicketSeatEntity, Integer> {
    // Define custom query methods if needed
    
}