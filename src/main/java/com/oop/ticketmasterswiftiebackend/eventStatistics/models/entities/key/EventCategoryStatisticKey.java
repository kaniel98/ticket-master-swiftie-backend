package com.oop.ticketmasterswiftiebackend.eventStatistics.models.entities.key;

import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EventCategoryStatisticKey implements Serializable {
    @Column(name = "eventStatisticId")
    @Schema(description = "Unique identifier for an event statistic", example = "123")
    private Integer eventStatisticId;

    @Column(name = "category")
    @Schema(description = "Category of the event venue area", example = "CAT1")
    @Enumerated(EnumType.STRING)
    private VenueAreaCategory category;
}
