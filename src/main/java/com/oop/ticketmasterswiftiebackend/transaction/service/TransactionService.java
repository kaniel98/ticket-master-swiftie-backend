package com.oop.ticketmasterswiftiebackend.transaction.service;

import com.oop.ticketmasterswiftiebackend.transaction.models.entities.TransactionEntity;
import com.oop.ticketmasterswiftiebackend.transaction.models.request.CreateTransactionRequest;
import com.oop.ticketmasterswiftiebackend.transaction.models.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest);

    TransactionResponse getTransactionById(Integer transactionId);

    List<TransactionResponse> getTransactionByTicketId(Integer ticketId);

    List<TransactionEntity> getAllSuccessfulChargeTransactionByTicketIds(List<Integer> ticketIds);

    void saveAllRefundTransactions(List<TransactionEntity> transactionEntities);

}
