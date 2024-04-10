package com.oop.ticketmasterswiftiebackend.seat.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.seat.constants.SeatError;
import com.oop.ticketmasterswiftiebackend.seat.constants.SeatStatus;
import com.oop.ticketmasterswiftiebackend.seat.models.entities.SeatEntity;
import com.oop.ticketmasterswiftiebackend.seat.models.request.CreateSeatRequest;
import com.oop.ticketmasterswiftiebackend.seat.models.request.UpdateSeatRequest;
import com.oop.ticketmasterswiftiebackend.seat.models.response.SeatResponse;
import com.oop.ticketmasterswiftiebackend.seat.repository.SeatRepository;
import com.oop.ticketmasterswiftiebackend.seat.service.SeatService;
import com.oop.ticketmasterswiftiebackend.venue.models.entities.EventVenueAreaEntity;
import com.oop.ticketmasterswiftiebackend.venue.repository.EventVenueAreaRepository;
import com.oop.ticketmasterswiftiebackend.venue.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final VenueService venueService;
    private final EventVenueAreaRepository eventVenueAreaRepository;
    private final ModelMapper mapper = new ModelMapper();
    private final String[] column = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF"
            , "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ"
    };

    @Override
    @Transactional(readOnly = true)
    public SeatResponse getSeatById(Integer seatId) {
        return mapper.map(getSeatEntityById(seatId), SeatResponse.class);
    }

    @Override
    public List<SeatResponse> getSeatByIds(List<Integer> seatIds) {
        return seatRepository.findBySeatIdIn(seatIds).stream()
                .map(seat -> mapper.map(seat, SeatResponse.class))
                .toList();
    }


    @Override
    @Async
    @Transactional
    public CompletableFuture<List<SeatResponse>> updateSeatStatus(UpdateSeatRequest request) {
        List<SeatEntity> seatsToUpdate = getAllSeatEntitiesBySeatId(request.getSeatIds());
        seatsToUpdate.forEach(seat -> seat.setStatus(request.getStatus()));
        try {
            log.info("Updating seat status");
            return CompletableFuture.completedFuture(
                    seatRepository.saveAll(seatsToUpdate).stream().map(seat -> mapper.map(seat, SeatResponse.class)).toList()
            );
        } catch (Exception e) {
            log.error("Error occurred when updating seat status: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    @Override
    @Transactional
    public List<SeatEntity> createSeats(CreateSeatRequest request) {
        // Check if there are enough seats first
        EventVenueAreaEntity eventVenueArea = venueService.getEventVenueAreaDetails(request.getEventVenueAreaId());
        if (request.getNumberOfSeats() > eventVenueArea.getNoOfSeat()) {
            throw new BaseException(SeatError.SEAT_NOT_ENOUGH.getCode(),
                    SeatError.SEAT_NOT_ENOUGH.getBusinessCode(), SeatError.SEAT_NOT_ENOUGH.getDescription());
        }

        // Proceed to allocate seats based on number of seats and columns
        int temp = eventVenueArea.getNoOfSeat();
        ArrayList<SeatEntity> seats = new ArrayList<>();
        int seatsPerRow = eventVenueArea.getNoOfCol();
        for (int i = 0; i < request.getNumberOfSeats(); i++) {
            temp--;
            int row = (int) Math.ceil(temp / seatsPerRow);
            int col = temp - (row * seatsPerRow);
            seats.add(SeatEntity.builder()
                    .eventVenueAreaId(request.getEventVenueAreaId())
                    .category(eventVenueArea.getCategory())
                    .status(SeatStatus.BOOKED)
                    .seatRow(column[row])
                    .seatCol(col + 1)
                    .build());
        }
        eventVenueArea.setNoOfSeat(eventVenueArea.getNoOfSeat() - request.getNumberOfSeats());
        try {
            venueService.updateEventVenueArea(eventVenueArea);
            return seatRepository.saveAll(seats);
        } catch (Exception e) {
            log.error("Error occurred when creating seats: {}", e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    // Region: Helper methods
    private SeatEntity getSeatEntityById(Integer seatId) {
        return seatRepository.findById(seatId).orElseThrow(() -> new BaseException(SeatError.SEAT_NOT_FOUND.getCode(),
                SeatError.SEAT_NOT_FOUND.getBusinessCode(), SeatError.SEAT_NOT_FOUND.getDescription()));
    }

    private List<SeatEntity> getAllSeatEntitiesBySeatId(List<Integer> seatIds) {
        return seatRepository.findBySeatIdIn(seatIds);
    }
    // End: Helper methods
}
