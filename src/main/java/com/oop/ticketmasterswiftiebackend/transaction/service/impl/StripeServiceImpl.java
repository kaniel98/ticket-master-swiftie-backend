package com.oop.ticketmasterswiftiebackend.transaction.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.ticket.service.TicketService;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionError;
import com.oop.ticketmasterswiftiebackend.transaction.models.entities.TransactionEntity;
import com.oop.ticketmasterswiftiebackend.transaction.service.StripeService;
import com.oop.ticketmasterswiftiebackend.transaction.service.TransactionService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class StripeServiceImpl implements StripeService {
    private final TransactionService transactionService;
    private final TicketService ticketService;

    @Value("${apiKeys.stripe}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    @Async // Asynchronously refund the transaction
    public void processEventGroupCancellationRefund(List<Integer> eventGroupDetailIds) {
        List<TransactionEntity> transactionResponseList = transactionService.getAllSuccessfulChargeTransactionByTicketIds(ticketService.listOfTicketIdForEventGroupDetails(eventGroupDetailIds));
        List<TransactionEntity> refundTransactionlist = new ArrayList<>();
        log.info("Processing refund for event group detail ids: {}", eventGroupDetailIds);
        // Refund the transaction
        transactionResponseList.forEach(transactionResponse -> {
            RefundCreateParams refundCreateParams = RefundCreateParams.builder()
                    .setPaymentIntent(String.valueOf(transactionResponse.getPaymentIntentId()))
                    .setAmount(transactionResponse.getAmount().longValue())
                    .build();
            try {
                Refund refund = Refund.create(refundCreateParams);
                // Create a refund for the transaction
                refundTransactionlist.add(TransactionEntity.builder()
                        .ticketId(transactionResponse.getTicketId())
                        .paymentIntentId(refund.getPaymentIntent())
                        .amount(Double.valueOf(refund.getAmount()))
                        .type(transactionResponse.getType())
                        .dateTime(CommonUtils.getCurrentTime())
                        .status(transactionResponse.getStatus())
                        .build());
            } catch (StripeException e) {
                log.error("Error occurred while refunding transaction: {}", e.getMessage());
                throw new BaseException(TransactionError.TRANSACTION_REFUND_ERROR.getCode(),
                        TransactionError.TRANSACTION_REFUND_ERROR.getBusinessCode(),
                        TransactionError.TRANSACTION_REFUND_ERROR.getDescription());
            } catch (Exception e) {
                log.error("Error occurred while refunding transaction: {}", e.getMessage());
                throw CommonUtils.commonExceptionHandler(e);
            }
        });
        transactionService.saveAllRefundTransactions(refundTransactionlist);
        log.info("Refund processed successfully for event group detail ids: {}", eventGroupDetailIds);
    }
}
