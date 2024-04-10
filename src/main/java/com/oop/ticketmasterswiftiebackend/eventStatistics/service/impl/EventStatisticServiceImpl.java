package com.oop.ticketmasterswiftiebackend.eventStatistics.service.impl;

import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.EventGroupDetailsWithSeatMapResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.FullEventGroupResponse;
import com.oop.ticketmasterswiftiebackend.event.service.EventService;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.EventCategoryStatisticEntity;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.EventStatisticEntity;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.key.EventCategoryStatisticKey;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.response.FullEventStatisticResponse;
import com.oop.ticketmasterswiftiebackend.eventStatistics.repository.EventCategoryStatisticRepository;
import com.oop.ticketmasterswiftiebackend.eventStatistics.repository.EventStatisticRepository;
import com.oop.ticketmasterswiftiebackend.eventStatistics.service.EventStatisticService;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketStatus;
import com.oop.ticketmasterswiftiebackend.ticket.models.response.FullTicketSeatResponse;
import com.oop.ticketmasterswiftiebackend.ticket.service.TicketService;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionStatus;
import com.oop.ticketmasterswiftiebackend.transaction.constants.TransactionType;
import com.oop.ticketmasterswiftiebackend.transaction.models.response.BaseTransactionResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseEventVenueAreaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventStatisticServiceImpl implements EventStatisticService {

    private final EventStatisticRepository eventStatisticRepository;
    private final EventCategoryStatisticRepository eventCategoryStatisticRepository;
    private final EventService eventService;
    private final TicketService ticketService;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    @Transactional
    public FullEventStatisticResponse getEventStatistic(Integer eventGroupId, Integer eventManagerId) {
        // Check if event exist
        FullEventGroupResponse eventGroupEntity = eventService.getFullEventGroupResponseById(eventGroupId);
        return eventStatisticRepository.findByEventGroupId(eventGroupId)
                .map(eventStatisticEntity1 -> mapper.map(eventStatisticEntity1, FullEventStatisticResponse.class))
                .orElseGet(() -> {
                    log.info("No event statistic found for event group id: {}, creating report for it now", eventGroupId);
                    return refreshEventStatistic(eventGroupId, eventGroupEntity, eventManagerId);
                });
    }

    @Override
    @Transactional
    public FullEventStatisticResponse refreshEventStatistic(Integer eventGroupId, FullEventGroupResponse eventGroup, Integer eventmanagerId) {
        EventStatisticEntity eventStatisticEntity = eventStatisticRepository.findByEventGroupId(eventGroupId).orElseGet(() -> EventStatisticEntity.builder().eventGroupId(eventGroupId)
                .eventManagerId(eventmanagerId)
                .totalAttendance(0)
                .totalRevenue(0.0)
                .totalSeatsSold(0)
                .totalTicketsSold(0)
                .build());

        List<EventCategoryStatisticEntity> existingCategoryStatisticEntityList = new ArrayList<>();
        if (eventStatisticEntity.getEventStatisticId() != null) {
            existingCategoryStatisticEntityList = eventCategoryStatisticRepository.findByIdEventStatisticId(eventStatisticEntity.getEventStatisticId());
        }
        if (eventGroup == null) {
            eventGroup = eventService.getFullEventGroupResponseById(eventGroupId);
        }
        // Hashmap to keep track of individual event category performance
        EnumMap<VenueAreaCategory, EventCategoryStatisticEntity> categoryStatisticEntityHashMap = new EnumMap<>(VenueAreaCategory.class);
        // Populate the hashmap with the EventVenueArea categories for the event
        populateEventStatisticCategoryHashMap(existingCategoryStatisticEntityList, eventGroup.getEventGroupDetails().getFirst().getVenueAreas(), categoryStatisticEntityHashMap, eventStatisticEntity.getEventStatisticId());

        // 1. For each event group details - Retrieve the tickets that are Active, Redeemed, Expired (As long as not cancelled)
        for (EventGroupDetailsWithSeatMapResponse eventGroupDetail : eventGroup.getEventGroupDetails()) {
            List<FullTicketSeatResponse> ticketSeatResponseList = ticketService.getAllValidTicketsSoldForEventGroupDetail(eventGroupDetail.getEventGroupDetailId());
            // 2. Each ticket should contain its corresponding transaction details - Amount sold paid for each ticket
            for (FullTicketSeatResponse ticket : ticketSeatResponseList) {
                // 3. For each ticket, we should be able to determine the category it belongs to
                VenueAreaCategory category = ticket.getTicketSeats().getFirst().getSeat().getCategory();
                // 4. For each category, we should be able to determine the total revenue, tickets sold, attendance
                EventCategoryStatisticEntity categoryStatisticEntity = categoryStatisticEntityHashMap.get(category);
                // 5. Update the category statistics
                for (BaseTransactionResponse transaction : ticket.getTransactions()) {
                    // 6. Transaction should be successful and not refunded
                    if (transaction.getType().equals(TransactionType.CHARGE) && transaction.getStatus().equals(TransactionStatus.SUCCESS)) {
                        // * Revenue
                        categoryStatisticEntity.setRevenue(categoryStatisticEntity.getRevenue() + transaction.getAmount());
                        // * Ticket sold
                        categoryStatisticEntity.setTicketsSold(categoryStatisticEntity.getTicketsSold() + 1);
                        // * Attendance
                        if (ticket.getStatus().equals(TicketStatus.REDEEMED)) { // Attendance is only counted if ticket is redeemed
                            categoryStatisticEntity.setAttendance(categoryStatisticEntity.getAttendance() + ticket.getTicketSeats().size());
                        }
                        // * Seats sold
                        categoryStatisticEntity.setSeatsSold(categoryStatisticEntity.getSeatsSold() + ticket.getTicketSeats().size());
                        break; // Only the first successful transaction is counted
                    }
                }
                // 7. Set the category statistics back to the hashmap
                categoryStatisticEntityHashMap.put(category, categoryStatisticEntity);
            }
        }

        // 8. Recalculate and tabulate the total revenue, tickets sold, attendance for the event
        calculateTotalEventStatistic(categoryStatisticEntityHashMap, eventStatisticEntity);
        eventStatisticEntity.setLastUpdated(CommonUtils.getCurrentTime());

        // 9. Save the event statistic
        try {
            eventStatisticEntity = eventStatisticRepository.save(eventStatisticEntity);

            // Proceed to save the individual categories statistics
            EventStatisticEntity finalEventStatisticEntity = eventStatisticEntity;
            List<EventCategoryStatisticEntity> updatedCategoryStatisticEntityList = eventCategoryStatisticRepository.saveAll(
                    categoryStatisticEntityHashMap.values().stream().map(
                            eventCategoryStatisticEntity -> {
                                eventCategoryStatisticEntity.getId().setEventStatisticId(finalEventStatisticEntity.getEventStatisticId());
                                eventCategoryStatisticEntity.setEventStatistic(finalEventStatisticEntity);
                                return eventCategoryStatisticEntity;
                            }).toList()
            );
            eventStatisticEntity.setEventCategoryStatistics(updatedCategoryStatisticEntityList);
            return mapper.map(eventStatisticEntity, FullEventStatisticResponse.class);
        } catch (Exception e) {
            log.error("Failed to save event statistic for event group id {}: {}", eventGroupId, e.getMessage());
            throw CommonUtils.commonExceptionHandler(e);
        }
    }

    private void populateEventStatisticCategoryHashMap(List<EventCategoryStatisticEntity> existingCategoryStatisticList, List<BaseEventVenueAreaResponse> venueAreaForEventGroup, EnumMap<VenueAreaCategory, EventCategoryStatisticEntity> categoryStatisticEntityHashMap, Integer eventStatisticEntityId) {
        if (existingCategoryStatisticList.isEmpty()) {
            for (BaseEventVenueAreaResponse venueArea : venueAreaForEventGroup) {
                categoryStatisticEntityHashMap.put(venueArea.getCategory(), EventCategoryStatisticEntity.builder()
                        .id(EventCategoryStatisticKey.builder()
                                .eventStatisticId(eventStatisticEntityId)
                                .category(venueArea.getCategory())
                                .build())
                        .revenue(0.0)
                        .ticketsSold(0)
                        .seatsSold(0)
                        .attendance(0)
                        .version(0)
                        .build());
            }
            return;
        }
        for (EventCategoryStatisticEntity categoryStatisticEntity : existingCategoryStatisticList) {
            categoryStatisticEntityHashMap.put(categoryStatisticEntity.getId().getCategory(), EventCategoryStatisticEntity.builder()
                    .id(EventCategoryStatisticKey.builder()
                            .eventStatisticId(eventStatisticEntityId)
                            .category(categoryStatisticEntity.getId().getCategory())
                            .build())
                    .revenue(0.0)
                    .ticketsSold(0)
                    .seatsSold(0)
                    .attendance(0)
                    .version(categoryStatisticEntity.getVersion())
                    .build());
        }
    }


    private void calculateTotalEventStatistic(EnumMap<VenueAreaCategory, EventCategoryStatisticEntity> categoryStatisticEntityHashMap, EventStatisticEntity eventStatistic) {
        // Reset the event statistic
        eventStatistic.setTotalRevenue(0.0);
        eventStatistic.setTotalTicketsSold(0);
        eventStatistic.setTotalSeatsSold(0);
        eventStatistic.setTotalAttendance(0);

        for (EventCategoryStatisticEntity categoryStatisticEntity : categoryStatisticEntityHashMap.values()) {
            eventStatistic.setTotalRevenue(eventStatistic.getTotalRevenue() + categoryStatisticEntity.getRevenue());
            eventStatistic.setTotalTicketsSold(eventStatistic.getTotalTicketsSold() + categoryStatisticEntity.getTicketsSold());
            eventStatistic.setTotalSeatsSold(eventStatistic.getTotalSeatsSold() + categoryStatisticEntity.getSeatsSold());
            eventStatistic.setTotalAttendance(eventStatistic.getTotalAttendance() + categoryStatisticEntity.getAttendance());
        }
    }


}
