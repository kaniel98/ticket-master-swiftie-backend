package com.oop.ticketmasterswiftiebackend.event.repository;

import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventGroupRepository extends JpaRepository<EventGroupEntity, Integer> {

    Page<EventGroupEntity> findByIsDeletedFalse(Pageable pageable);

    Optional<EventGroupEntity> findByEventGroupIdAndIsDeletedFalse(Integer eventGroupId);
}
