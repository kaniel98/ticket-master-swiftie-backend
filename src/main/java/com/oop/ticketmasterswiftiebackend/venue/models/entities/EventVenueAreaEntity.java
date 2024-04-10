package com.oop.ticketmasterswiftiebackend.venue.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupDetailEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`EventVenueArea`")
@EqualsAndHashCode(exclude = "venueAreas")
@ToString(exclude = "venueAreas")
public class EventVenueAreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for a event venue area", example = "123")
    private Integer eventVenueAreaId;

    @Schema(description = "Unique identifier for a venue", example = "123")
    private Integer venueId;

    @Schema(description = "Unique identifier for a event group detail", example = "123")
    private Integer eventGroupDetailId;

    @Schema(description = "Category type", example = "CAT1")
    @Enumerated(EnumType.STRING)
    private VenueAreaCategory category;

    @Schema(description = "Number of seats", example = "300")
    private Integer noOfSeat;

    @Schema(description = "Number of columns", example = "3")
    private Integer noOfCol;

    @Schema(description = "Number of rows", example = "3")
    private Integer noOfRow;

    @Schema(description = "Price for given category", example = "100.00")
    private Double price;

    @Schema(description = "Position identifier for the given venue area", example = "CAT1_1")
    private String position;

    @ManyToOne(targetEntity = EventGroupDetailEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "eventGroupDetailId", insertable = false, updatable = false)
    @JsonBackReference
    private EventGroupDetailEntity venueAreas;
}
