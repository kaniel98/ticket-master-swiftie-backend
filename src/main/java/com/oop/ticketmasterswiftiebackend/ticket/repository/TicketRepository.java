package com.oop.ticketmasterswiftiebackend.ticket.repository;

import com.oop.ticketmasterswiftiebackend.ticket.models.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {
    List<TicketEntity> findByCustomerId(Integer customerId);

    List<TicketEntity> findByEventGroupDetailIdAndStatusIn(Integer eventGroupDetailId, List<String> status);

    List<TicketEntity> findByEventGroupDetailIdInAndStatusIn(List<Integer> eventGroupDetailIds, List<String> status);
} 