package com.oop.ticketmasterswiftiebackend.seat.service;

import com.oop.ticketmasterswiftiebackend.seat.models.entities.SeatEntity;
import com.oop.ticketmasterswiftiebackend.seat.models.request.CreateSeatRequest;
import com.oop.ticketmasterswiftiebackend.seat.models.request.UpdateSeatRequest;
import com.oop.ticketmasterswiftiebackend.seat.models.response.SeatResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SeatService {

    List<SeatEntity> createSeats(CreateSeatRequest createSeatRequest);

    CompletableFuture<List<SeatResponse>> updateSeatStatus(UpdateSeatRequest updateSeatRequest);

    SeatResponse getSeatById(Integer seatId);

    List<SeatResponse> getSeatByIds(List<Integer> seatIds);
}
