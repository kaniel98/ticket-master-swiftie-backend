package com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.key.EventCategoryStatisticKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`EventCategoryStatistic`")
@EqualsAndHashCode(exclude = "eventStatistic")
@ToString(exclude = "eventStatistic")
public class EventCategoryStatisticEntity {
    @EmbeddedId
    private EventCategoryStatisticKey id;

    @Schema(description = "Total revenue for an event category", example = "1000.00")
    private Double revenue;

    @Schema(description = "Total tickets sold for an event category", example = "100")
    private Integer ticketsSold;

    @Schema(description = "Total seats sold for an event category", example = "100")
    private Integer seatsSold;

    @Schema(description = "Attendance for the an event category", example = "100")
    private Integer attendance;

    // Used to enforce optimistic locking
    @Version
    private Integer version;

    @ManyToOne(targetEntity = EventStatisticEntity.class, fetch = FetchType.LAZY)
    @MapsId("eventStatisticId")
    @JoinColumn(name = "eventStatisticId", insertable = false, updatable = false)
    @JsonBackReference
    private EventStatisticEntity eventStatistic;

}
