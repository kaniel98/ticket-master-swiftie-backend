package com.oop.ticketmasterswiftiebackend.notification.service;

import com.oop.ticketmasterswiftiebackend.notification.models.request.ContactRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketNotificationResponse;


public interface NotificationService {

    void sendEmailNotificationForTicket(TicketNotificationResponse ticketNotificationResponse);

    void createContact(ContactRequest request);
}
