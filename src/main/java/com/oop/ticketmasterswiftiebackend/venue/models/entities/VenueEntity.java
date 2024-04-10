package com.oop.ticketmasterswiftiebackend.venue.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oop.ticketmasterswiftiebackend.event.models.entities.EventGroupEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`Venue`")
@EqualsAndHashCode(exclude = {"events", "seatMap"})
@ToString(exclude = {"events", "seatMap"})
public class VenueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for an venue", example = "123")
    private Integer venueId;

    @Schema(description = "Venue name", example = "Sports Hall 1")
    private String name;

    @Schema(description = "Venue address", example = "1234 Main Street")
    private String address;

    @Schema(description = "Indicate if the venue is deleted", example = "false")
    private Boolean isDeleted;

    // Cascade - if the venue is deleted, the seat map is also deleted
    // MappedBy - the VenueAreaEntity has a foreign key to the VenueEntity
    @OneToMany(mappedBy = "venue", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of venue areas", example = "[]")
    private List<VenueAreaEntity> seatMap;

    @OneToMany(mappedBy = "venue", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of events", example = "[]")
    private List<EventGroupEntity> events;
}
