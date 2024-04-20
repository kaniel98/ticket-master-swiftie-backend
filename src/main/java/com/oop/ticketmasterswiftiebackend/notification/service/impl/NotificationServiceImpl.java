package com.oop.ticketmasterswiftiebackend.notification.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.notification.constants.NotificationError;
import com.oop.ticketmasterswiftiebackend.notification.models.request.ContactRequest;
import com.oop.ticketmasterswiftiebackend.notification.service.NotificationService;
import com.oop.ticketmasterswiftiebackend.seat.models.response.SeatResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketNotificationResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketSeatResponse;
import com.oop.ticketmasterswiftiebackend.user.models.entities.UserEntity;
import com.oop.ticketmasterswiftiebackend.user.service.UserService;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.ContactsApi;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateContact;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final UserService userService;
    private final ContactsApi contactApi = new ContactsApi();
    private final TransactionalEmailsApi transactionalEmailsApi = new TransactionalEmailsApi();
    private ApiKeyAuth apiKeyAuth;
    private ApiClient defaultClient;
    @Value("${apiKeys.brevo}")
    private String brevoApiKey;

    @PostConstruct
    public void init() {
        defaultClient = Configuration.getDefaultApiClient();
        apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(brevoApiKey);
    }

    @Override
    @Async
    public void sendEmailNotificationForTicket(TicketNotificationResponse ticketResponse) {

        UserEntity customer = userService.getUserById(ticketResponse.getCustomerId());
        String customerEmail = customer.getEmail();
        checkCustomerContactDetails(customerEmail);

        // * Constructing the email template
        List<Map<String, Object>> tickets = new ArrayList<>();
        List<Map<String, Object>> events = new ArrayList<>();

        // Form the seat string
        List<String> seats = new ArrayList<>();
        for (TicketSeatResponse ticketSeatResponse : ticketResponse.getTicketSeats()) {
            SeatResponse seatResponse = ticketSeatResponse.getSeat();
            String seatRow = seatResponse.getSeatRow();
            Integer seatCol = seatResponse.getSeatCol();
            String rowColumn = seatRow + seatCol;
            seats.add(rowColumn); // RowColumn format P5 = Row P Col 5
        }
        String seatString = String.join(",", seats);

        // * Forming ticket object recipient be shown in the email
        Map<String, Object> ticketObj = new HashMap<>();
        ticketObj.put("ticketId", ticketResponse.getTicketId());
        ticketObj.put("bookingMethod", ticketResponse.getBookingMethod());
        ticketObj.put("qrCodeImageUrl", ticketResponse.getQrCodeImageUrl());
        ticketObj.put("numberOfGuests", ticketResponse.getNumberOfGuests());
        ticketObj.put("category", ticketResponse.getTicketSeats().getFirst().getSeat().getCategory());
        ticketObj.put("seats", seatString);
        tickets.add(ticketObj);

        // EventGroupDetail related info
        BaseVenueResponse venue = ticketResponse.getEventGroupDetail().getEventGroup().getVenue();

        // * Forming event object recipient be shown in the email
        Map<String, Object> eventObj = new HashMap<>();
        eventObj.put("name", ticketResponse.getEventGroupDetail().getEventGroup().getName());
        eventObj.put("venueName", ticketResponse.getEventGroupDetail().getEventGroup().getVenue().getName());
        eventObj.put("venueAddress", venue.getAddress());
        eventObj.put("dateTime", ticketResponse.getEventGroupDetail().getDateTime().toString());
        eventObj.put("posterImgUrl", ticketResponse.getEventGroupDetail().getEventGroup().getPosterImgUrl());
        events.add(eventObj);

        // * Constructing the email
        String emailSubject = "Ticket Booking Confirmation";

        // * Recipient information
        SendSmtpEmailTo recipient = new SendSmtpEmailTo();
        recipient.setEmail(customer.getEmail());
        recipient.setName(customer.getUsername()); // Update with recipient's name if available
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.setTo(List.of(recipient));
        sendSmtpEmail.setSubject(emailSubject);
        sendSmtpEmail.setTemplateId(2L); // Using Brevo template ID 2

        // * Email content
        Map<String, Object> params = new HashMap<>();
        params.put("username", customer.getUsername());
        params.put("tickets", tickets);
        params.put("events", events);
        sendSmtpEmail.setParams(params);

        // * Send email notification
        sendEmailNotification(sendSmtpEmail);
    }

    private void sendEmailNotification(SendSmtpEmail email) {
        try {
            transactionalEmailsApi.sendTransacEmail(email);
        } catch (ApiException e) {
            log.error("Error occurred while sending email notification: {}", e.getMessage());
            throw new BaseException(NotificationError.EMAIL_SEND_ERROR.getCode(), NotificationError.EMAIL_SEND_ERROR.getBusinessCode(), NotificationError.EMAIL_SEND_ERROR.getDescription());
        } catch (Exception e) {
            log.error("Generic error occurred while sending email notification: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }


    // Checks if the customer email is present in Brevo, if not creates a new contact
    @SneakyThrows // Allows the code to throw checked exceptions without declaring them
    private void checkCustomerContactDetails(String customerEmail) {
        try {
            contactApi.getContactInfo(customerEmail, null, null);
        } catch (ApiException apiException) {
            log.info("Contact not found for email: {}", customerEmail);
            // if contact don't exist, create one
            createContact(ContactRequest.builder()
                    .email(customerEmail)
                    .build());
        }
    }


    @Override
    public void createContact(ContactRequest request) {
        try {
            CreateContact createContact = new CreateContact();
            createContact.setEmail(request.getEmail());
            createContact.setEmailBlacklisted(false);
            createContact.setSmsBlacklisted(false);
            createContact.setUpdateEnabled(false);
            // Proceed to create contact
            contactApi.createContact(createContact);
        } catch (ApiException e) {
            log.error("Error occurred while creating contact for email {}: {}", request.getEmail(), e.getMessage());
            throw new BaseException(NotificationError.CONTACT_CREATE_ERROR.getCode(), NotificationError.CONTACT_CREATE_ERROR.getBusinessCode(), NotificationError.CONTACT_CREATE_ERROR.getDescription());
        } catch (Exception e) {
            log.error("Error occurred while creating contact for email {}: {}", request.getEmail(), e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }
}
