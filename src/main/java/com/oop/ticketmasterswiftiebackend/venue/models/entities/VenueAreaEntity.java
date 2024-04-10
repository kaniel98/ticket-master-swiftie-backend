package com.oop.ticketmasterswiftiebackend.venue.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`VenueArea`")
@EqualsAndHashCode(exclude = "venue")
@ToString(exclude = "venue")
public class VenueAreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for a venue area", example = "123")
    private Integer venueAreaId;

    @Schema(description = "Unique identifier for a venue", example = "123")
    private Integer venueId;

    @Schema(description = "Category type", example = "CAT1")
    @Enumerated(EnumType.STRING)
    private VenueAreaCategory category;

    @Schema(description = "Number of seats", example = "300")
    private Integer noOfSeat;

    @Schema(description = "Number of columns", example = "3")
    private Integer noOfCol;

    @Schema(description = "Number of rows", example = "3")
    private Integer noOfRow;

    @Schema(description = "Position identifier for the given venue area", example = "CAT1_1")
    private String position;

    @Schema(description = "Indicate if the venue area is deleted", example = "false")
    private Boolean isDeleted;

    @ManyToOne(targetEntity = VenueEntity.class, fetch = FetchType.LAZY)
    @MapsId("venueId")
    @JoinColumn(name = "venueId", insertable = false, updatable = false)
    @JsonBackReference
    private VenueEntity venue;
}
