package com.oop.ticketmasterswiftiebackend.eventStatistics.service;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.FullEventGroupResponse;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.response.FullEventStatisticResponse;

public interface EventStatisticService {
    // * Used to get the event statistics for a single event
    // * Revenue, total tickets sold, attendance, etc.
    FullEventStatisticResponse getEventStatistic(Integer eventGroupId, Integer eventManagerId);

    // * Used to refresh the event statistics for a single event
    FullEventStatisticResponse refreshEventStatistic(Integer eventGroupId, FullEventGroupResponse eventGroupEntity, Integer eventManagerId);
}
