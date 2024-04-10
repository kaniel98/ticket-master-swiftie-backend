package com.oop.ticketmasterswiftiebackend.transaction.service;

import java.util.List;

public interface StripeService {
    void processEventGroupCancellationRefund(List<Integer> eventGroupDetailIds);
}
