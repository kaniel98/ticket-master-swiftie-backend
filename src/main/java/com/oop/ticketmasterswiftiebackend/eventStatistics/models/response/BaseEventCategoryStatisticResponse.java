package com.oop.ticketmasterswiftiebackend.eventStatistics.models.response;

import com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.key.EventCategoryStatisticKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BaseEventCategoryStatisticResponse {
    private EventCategoryStatisticKey id;

    @Schema(description = "Total revenue for an event category", example = "1000.00")
    private Double revenue;

    @Schema(description = "Total tickets sold for an event category", example = "100")
    private Integer ticketsSold;

    @Schema(description = "Total seats sold for an event category", example = "100")
    private Integer seatsSold;

    @Schema(description = "Attendance for the an event category", example = "100.00")
    private double attendance;

}
