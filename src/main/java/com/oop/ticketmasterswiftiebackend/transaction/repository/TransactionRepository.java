package com.oop.ticketmasterswiftiebackend.transaction.repository;

import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionStatus;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionType;
import com.oop.ticketmasterswiftiebackend.transaction.models.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    List<TransactionEntity> findByTicketId(Integer ticketId);

    List<TransactionEntity> findAllByTicketIdInAndStatusAndType(List<Integer> ticketIds, TransactionStatus status, TransactionType type);
}
