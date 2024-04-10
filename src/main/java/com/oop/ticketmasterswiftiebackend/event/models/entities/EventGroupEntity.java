package com.oop.ticketmasterswiftiebackend.event.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oop.ticketmasterswiftiebackend.event.constants.EventStatus;
import com.oop.ticketmasterswiftiebackend.event.constants.EventType;
import com.oop.ticketmasterswiftiebackend.venue.models.entities.VenueEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`EventGroup`")
@EqualsAndHashCode(exclude = {"venue", "eventGroupDetails"})
@ToString(exclude = {"venue", "eventGroupDetails"})
public class EventGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for an event", example = "123")
    private Integer eventGroupId;

    @Schema(description = "Event name", example = "Taylor Swift Concert")
    private String name;

    @Schema(description = "Event type", example = "MUSICAL_CONCERT")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Schema(description = "Indicate if event is open for booking", example = "true")
    private Boolean bookingAllowed;

    @Schema(description = "Cancellation fee", example = "10.00")
    private Double cancellationFee;

    @Schema(description = "Event description", example = "ACTIVE")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Schema(description = "Event Manager's ID", example = "1")
    private Integer eventManagerId;

    @Schema(description = "Image URL for the event poster", example = "www.image.com")
    private String posterImgUrl;

    @Schema(description = "Image URL for the event banner", example = "www.image.com")
    private String bannerImgUrl;

    @Schema(description = "Event status")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Schema(description = "Indicate if the image was deleted", example = "true")
    private boolean isDeleted;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "eventGroup", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of event group pricing", example = "[]")
    private List<EventGroupPricingEntity> eventGroupPricing;

    @ManyToOne(targetEntity = VenueEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "venueId")
    @JsonBackReference
    private VenueEntity venue;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "eventGroup", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of event group details - e.g., Tour dates", example = "[]")
    private List<EventGroupDetailEntity> eventGroupDetails;
}
