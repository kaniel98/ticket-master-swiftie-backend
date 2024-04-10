package com.oop.ticketmasterswiftiebackend.event.models.entities;

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
@Table(name = "`EventGroupPricing`")
@EqualsAndHashCode(exclude = {"eventGroup"})
@ToString(exclude = {"eventGroup"})
public class EventGroupPricingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for an event group pricing", example = "123")
    private Integer eventGroupPricingId;

    @Schema(description = "Event group ID", example = "1")
    private Integer eventGroupId;

    @Schema(description = "Venue area category", example = "VIP")
    @Enumerated(EnumType.STRING)
    private VenueAreaCategory category;

    @Schema(description = "Price for the given category", example = "100.00")
    private Double price;

    @ManyToOne(targetEntity = EventGroupEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "eventGroupId", insertable = false, updatable = false)
    @JsonBackReference
    private EventGroupEntity eventGroup;
}
