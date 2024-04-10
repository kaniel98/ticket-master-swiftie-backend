package com.oop.ticketmasterswiftiebackend.ticket.service;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.external.service.QRCodeGeneratorService;
import com.oop.ticketmasterswiftiebackend.media.service.MediaService;
import com.oop.ticketmasterswiftiebackend.notification.service.NotificationService;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketError;
import com.oop.ticketmasterswiftiebackend.ticket.models.entities.TicketEntity;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketNotificationResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketServiceUtil {
    private final MediaService mediaService;
    private final QRCodeGeneratorService qrCodeGeneratorService;
    private final NotificationService notificationService;
    private final TicketRepository ticketRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Value("${aws.s3.qr-code-image-folder}")
    private String qrCodeImageFolder;

    @Transactional
    @Async
    public void generateQRCodeAndSaveToTicket(TicketWithSeatsResponse ticket, Boolean sendEmail) {
        String qrCodeText = "{\n    \"ticketId\": " + ticket.getCustomerId() + ",\n    \"customerId\":  " + ticket.getTicketId() + "\n}";
        ByteArrayResource qrCode = qrCodeGeneratorService.generateQRCodeImage(qrCodeText, 250, 250);
        String qrCodeImageUrl = mediaService.uploadQrCodeImage(qrCodeImageFolder, qrCode, MediaType.IMAGE_PNG);
        log.info("QR code image uploaded to: {}", qrCodeImageUrl);
        TicketEntity ticketEntity = getTicketEntityById(ticket.getTicketId());
        ticketEntity.setQrCodeImageUrl(qrCodeImageUrl);
        try {
            ticketRepository.save(ticketEntity);
            if (Boolean.TRUE.equals(sendEmail)) {
                sendTicketEmail(ticket.getTicketId());
            }
            log.info("QR code image url saved to ticket: {}", ticketEntity.getTicketId());
        } catch (Exception error) {
            log.info("Error saving QR code image to ticket: {}", ticket.getTicketId(), error);
            throw CommonUtils.commonExceptionHandler(error);
        }
    }

    @Transactional
    public TicketEntity getTicketEntityById(Integer ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BaseException(TicketError.TICKET_NOT_FOUND.getCode(),
                        TicketError.TICKET_NOT_FOUND.getBusinessCode(), TicketError.TICKET_NOT_FOUND.getDescription()));
    }

    @Transactional
    public void sendTicketEmail(Integer ticketId) {
        TicketNotificationResponse ticket = mapper.map(getTicketEntityById(ticketId), TicketNotificationResponse.class);
        notificationService.sendEmailNotificationForTicket(ticket);
    }
}
