package com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`EventStatistic`")
public class EventStatisticEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Schema(description = "Total seats sold for an event", example = "100")
    private Integer totalSeatsSold;

    @Schema(description = "Attendance for the given event", example = "100.00")
    private Integer totalAttendance;

    // Used to enforce optimistic locking
    @Version
    private Integer version;

    @Schema(description = "The time which the statistic was generated", example = "2022-12-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "eventStatistic", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of event category statistics", example = "[]")
    private List<EventCategoryStatisticEntity> eventCategoryStatistics;
}
