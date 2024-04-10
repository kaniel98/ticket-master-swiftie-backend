package com.oop.ticketmasterswiftiebackend.transaction.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionError;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionStatus;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionType;
import com.oop.ticketmasterswiftiebackend.transaction.models.entities.TransactionEntity;
import com.oop.ticketmasterswiftiebackend.transaction.models.request.CreateTransactionRequest;
import com.oop.ticketmasterswiftiebackend.transaction.models.response.TransactionResponse;
import com.oop.ticketmasterswiftiebackend.transaction.repository.TransactionRepository;
import com.oop.ticketmasterswiftiebackend.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest) {
        log.info("Creating transaction for: {}", createTransactionRequest);
        TransactionEntity newTransaction = TransactionEntity.builder()
                .ticketId(createTransactionRequest.getTicketId())
                .paymentIntentId(createTransactionRequest.getPaymentIntentId())
                .amount(createTransactionRequest.getAmount() * 100)
                .dateTime(createTransactionRequest.getDateTime())
                .status(createTransactionRequest.getStatus())
                .type(createTransactionRequest.getType())
                .build();
        try {
            TransactionEntity savedTransaction = transactionRepository.save(newTransaction);
            return mapper.map(savedTransaction, TransactionResponse.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate data detected: {}", e.getMessage());
            throw new BaseException(TransactionError.DUPLICATE_TRANSACTION.getCode(),
                    TransactionError.DUPLICATE_TRANSACTION.getBusinessCode(),
                    TransactionError.DUPLICATE_TRANSACTION.getDescription());
        } catch (Exception e) {
            log.error("Error occurred while creating transaction: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Integer transactionId) {
        TransactionEntity transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BaseException(
                        TransactionError.TRANSACTION_NOT_FOUND.getCode(),
                        TransactionError.TRANSACTION_NOT_FOUND.getBusinessCode(),
                        TransactionError.TRANSACTION_NOT_FOUND.getDescription()));
        return mapper.map(transaction, TransactionResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionByTicketId(Integer ticketId) {
        List<TransactionEntity> transactions = transactionRepository.findByTicketId(ticketId);
        if (transactions.isEmpty()) {
            throw new BaseException(TransactionError.TRANSACTION_NOT_FOUND.getCode(),
                    TransactionError.TRANSACTION_NOT_FOUND.getBusinessCode(),
                    TransactionError.TRANSACTION_NOT_FOUND.getDescription());
        }
        return transactions.stream()
                .map(transaction -> mapper.map(transaction, TransactionResponse.class))
                .toList();
    }

    @Override
    public List<TransactionEntity> getAllSuccessfulChargeTransactionByTicketIds(List<Integer> ticketIds) {
        return transactionRepository.findAllByTicketIdInAndStatusAndType(ticketIds, TransactionStatus.SUCCESS, TransactionType.CHARGE);
    }

    @Override
    @Async
    public void saveAllRefundTransactions(List<TransactionEntity> transactionEntities) {
        try {
            transactionRepository.saveAll(transactionEntities);
        } catch (Exception e) {
            log.error("Error occurred while saving refund transactions: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }
}
