package com.oop.ticketmasterswiftiebackend.ticket.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.seat.constants.SeatStatus;
import com.oop.ticketmasterswiftiebackend.seat.models.entities.SeatEntity;
import com.oop.ticketmasterswiftiebackend.seat.models.entities.TicketSeatEntity;
import com.oop.ticketmasterswiftiebackend.seat.models.request.CreateSeatRequest;
import com.oop.ticketmasterswiftiebackend.seat.models.request.UpdateSeatRequest;
import com.oop.ticketmasterswiftiebackend.seat.repository.TicketSeatRepository;
import com.oop.ticketmasterswiftiebackend.seat.service.SeatService;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketError;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketStatus;
import com.oop.ticketmasterswiftiebackend.ticket.models.entities.TicketEntity;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.BookingTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.request.UpdateTicketRequest;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.BaseTicketResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.FullTicketSeatResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketSeatResponse;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.TicketWithSeatsResponse;
import com.oop.ticketmasterswiftiebackend.ticket.repository.TicketRepository;
import com.oop.ticketmasterswiftiebackend.ticket.service.TicketService;
import com.oop.ticketmasterswiftiebackend.ticket.service.TicketServiceUtil;
import com.oop.ticketmasterswiftiebackend.user.constants.RoleName;
import com.oop.ticketmasterswiftiebackend.user.models.request.VerifyTicketValidityRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final SeatService seatService;
    private final TicketServiceUtil ticketServiceUtil;
    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final ModelMapper mapper = new ModelMapper();

    // * Ticketing officer booking ticket flow
    @Override
    @Transactional
    public TicketWithSeatsResponse createTicket(BookingTicketRequest ticketBookingRequest, Boolean sendEmail) {
        try {
            // 1. Create seats
            List<SeatEntity> seats = seatService.createSeats(CreateSeatRequest.builder()
                    .eventVenueAreaId(ticketBookingRequest.getEventVenueAreaId())
                    .numberOfSeats(ticketBookingRequest.getNumberOfGuests())
                    .build());

            // 2. Create ticket
            TicketWithSeatsResponse savedTicket = mapper.map(ticketRepository.save(TicketEntity.builder()
                    .eventGroupDetailId(ticketBookingRequest.getEventGroupDetailId())
                    .customerId(ticketBookingRequest.getCustomerId())
                    .status(TicketStatus.ACTIVE)
                    .eventGroupDetailId(ticketBookingRequest.getEventGroupDetailId())
                    .bookingMethod(ticketBookingRequest.getBookingMethod())
                    .numberOfGuests(ticketBookingRequest.getNumberOfGuests())
                    .build()), TicketWithSeatsResponse.class);

            // 3. Proceed to save it to TicketSeat table
            List<TicketSeatResponse> ticketSeatResponses = ticketSeatRepository.saveAll(seats.stream()
                    .map(seat -> TicketSeatEntity.builder()
                            .ticketId(savedTicket.getTicketId())
                            .seatId(seat.getSeatId())
                            .seat(seat)
                            .build())
                    .toList()).stream().map(ticketSeat -> mapper.map(ticketSeat, TicketSeatResponse.class)).toList();
            savedTicket.setTicketSeats(ticketSeatResponses);

            // Asynchronously generate QR Code & Save it to the ticket
            ticketServiceUtil.generateQRCodeAndSaveToTicket(savedTicket, sendEmail);

            return savedTicket;
        } catch (Exception e) {
            log.error("Error occurred while creating ticket: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public TicketWithSeatsResponse getTicketById(Integer ticketId) {
        return mapper.map(ticketServiceUtil.getTicketEntityById(ticketId), TicketWithSeatsResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketWithSeatsResponse> getTicketsByCustomerId(Integer customerId) {
        return ticketRepository.findByCustomerId(customerId).stream()
                .map(ticket -> mapper.map(ticket, TicketWithSeatsResponse.class))
                .toList();
    }

    // * Used for individual cancels - Usually by customer
    // * Can also be used by ticketing officer / admin to cancel ticket
    @Override
    @Transactional
    public BaseTicketResponse cancelTicketById(UpdateTicketRequest request, RoleName cancelledBy) {
        TicketEntity ticketToCancel = ticketServiceUtil.getTicketEntityById(request.getTicketId());
        if (ticketToCancel.getStatus() != TicketStatus.ACTIVE) {
            log.error("Ticket cannot be cancelled. Ticket status is not ACTIVE");
            throw new BaseException(TicketError.TICKET_CANNOT_BE_CANCELLED.getCode(),
                    TicketError.TICKET_CANNOT_BE_CANCELLED.getBusinessCode(),
                    TicketError.TICKET_CANNOT_BE_CANCELLED.getDescription());
        }

        if (cancelledBy == RoleName.USER) {
            ticketToCancel.setStatus(TicketStatus.CANCELLED_BY_CUSTOMER);
        } else if (cancelledBy == RoleName.TICKETING_OFFICER) {
            ticketToCancel.setStatus(TicketStatus.CANCELLED_BY_ORGANISER);
        }

        UpdateSeatRequest updateSeatRequest = UpdateSeatRequest.builder()
                .seatIds(ticketToCancel.getTicketSeats().stream().map(TicketSeatEntity::getSeatId).toList())
                .status(SeatStatus.AVAILABLE)
                .build();

        try {
            // Cancel the seats associated with the ticket
            seatService.updateSeatStatus(updateSeatRequest); // Will be executed asynchronously
            // Proceed to cancel the ticket
            log.info("Cancelling ticket with id {}", request.getTicketId());
            return mapper.map(ticketRepository.save(ticketToCancel), BaseTicketResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while cancelling ticket: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    public BaseTicketResponse updateTicketQrCodeImage(String qrCodeImageUrl, Integer ticketId) {
        TicketEntity ticket = ticketServiceUtil.getTicketEntityById(ticketId);
        ticket.setQrCodeImageUrl(qrCodeImageUrl);
        try {
            return mapper.map(ticketRepository.save(ticket), BaseTicketResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while updating ticket QR code image: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TicketWithSeatsResponse verifyTicketValidity(VerifyTicketValidityRequest verifyTicketValidityRequest) {
        TicketEntity ticket = ticketServiceUtil.getTicketEntityById(verifyTicketValidityRequest.getTicketId());
        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            log.error("Ticket with id {} is not valid for the event - Not active ticket", verifyTicketValidityRequest.getTicketId());
            throw new BaseException(TicketError.TICKET_NOT_ACTIVE.getCode(),
                    ticket.getStatus().toString(), // Return the status of the ticket
                    TicketError.TICKET_NOT_ACTIVE.getDescription());
        }
        return mapper.map(ticket, TicketWithSeatsResponse.class);
    }

    @Override
    @Transactional
    public BaseTicketResponse redeemTicket(UpdateTicketRequest updateTicketRequest) {
        TicketEntity ticketToBeRedeemed = ticketServiceUtil.getTicketEntityById(updateTicketRequest.getTicketId());
        if (ticketToBeRedeemed.getStatus() != TicketStatus.ACTIVE) {
            log.error("Ticket cannot be cancelled. Ticket status is not ACTIVE");
            throw new BaseException(TicketError.TICKET_CANNOT_BE_CANCELLED.getCode(),
                    TicketError.TICKET_CANNOT_BE_CANCELLED.getBusinessCode(),
                    TicketError.TICKET_CANNOT_BE_CANCELLED.getDescription());
        }
        ticketToBeRedeemed.setStatus(TicketStatus.REDEEMED);
        try {
            return mapper.map(ticketRepository.save(ticketToBeRedeemed), BaseTicketResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while redeeming ticket: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    // * Used to get tickets that are Active/Redeemed/Expired (As long as not cancelled) for event group detail
    @Override
    public List<FullTicketSeatResponse> getAllValidTicketsSoldForEventGroupDetail(Integer eventGroupDetailId) {
        return ticketRepository.findByEventGroupDetailIdAndStatusIn(eventGroupDetailId,
                List.of(TicketStatus.ACTIVE.name(), TicketStatus.REDEEMED.name(), TicketStatus.EXPIRED.name())).stream().map(
                ticketEntity -> mapper.map(ticketEntity, FullTicketSeatResponse.class)
        ).toList();
    }

    @Override
    public List<Integer> listOfTicketIdForEventGroupDetails(List<Integer> eventGroupDetailIds) {
        return ticketRepository.findByEventGroupDetailIdInAndStatusIn(eventGroupDetailIds,
                        List.of(TicketStatus.ACTIVE.name())).stream()
                .map(TicketEntity::getTicketId)
                .toList();
    }

    @Override
    @Async
    public void cancelTicketsByEventGroupDetails(List<Integer> eventGroupDetailsIds) {
        log.info("Cancelling the tickets for given event group details");
        try {
            ticketRepository.saveAll(
                    ticketRepository.findByEventGroupDetailIdInAndStatusIn(eventGroupDetailsIds, List.of(TicketStatus.ACTIVE.name())).stream().map(
                            ticketEntity -> {
                                ticketEntity.setStatus(TicketStatus.CANCELLED_BY_ORGANISER);
                                return ticketEntity;
                            }
                    ).toList()
            );

        } catch (Exception e) {
            log.error("Error cancelling the events for given event group details: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }
}
