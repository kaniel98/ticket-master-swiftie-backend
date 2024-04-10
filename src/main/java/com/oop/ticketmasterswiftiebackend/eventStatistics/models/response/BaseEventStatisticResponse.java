package com.oop.ticketmasterswiftiebackend.eventStatistics.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Schema(name = "BaseEventStatisticResponse", description = "Base response for event statistic")
public class BaseEventStatisticResponse {
    @Schema(description = "Unique identifier for an event statistic", example = "123")
    private Integer eventStatisticId;

    @Schema(description = "Event Group Id - Unique identifier for an event group", example = "123")
    private Integer eventGroupId;

    @Schema(description = "Unique identifier for an event manager", example = "123")
    private Integer eventManagerId;

    @Schema(description = "Total revenue for event", example = "1000.00")
    private Double totalRevenue;

    @Schema(description = "Total tickets sold for an event", example = "100")
    private Integer totalTicketsSold;

    @Schema(description = "Attendance for the given event", example = "100.00")
    private double totalAttendance;

    @Schema(description = "Total seats sold for an event", example = "100")
    private Integer totalSeatsSold;

    @Schema(description = "The time which the statistic was generated", example = "2022-12-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;
}
