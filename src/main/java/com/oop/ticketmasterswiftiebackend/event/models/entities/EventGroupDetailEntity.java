package com.oop.ticketmasterswiftiebackend.event.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oop.ticketmasterswiftiebackend.ticket.models.entities.TicketEntity;
import com.oop.ticketmasterswiftiebackend.venue.models.entities.EventVenueAreaEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`EventGroupDetail`")
@EqualsAndHashCode(exclude = {"eventGroup", "venueAreas", "tickets"})
@ToString(exclude = {"eventGroup", "venueAreas", "tickets"})
public class EventGroupDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the event group detail", example = "123")
    private Integer eventGroupDetailId;

    @Schema(description = "Event date time", example = "2022-12-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    @Schema(description = "Event group id", example = "123")
    private Integer eventGroupId;

    @ManyToOne(targetEntity = EventGroupEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "eventGroupId", insertable = false, updatable = false)
    @JsonBackReference
    private EventGroupEntity eventGroup;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "eventGroupDetailId", fetch = FetchType.LAZY)
    @Schema(description = "List of event venue areas", example = "[]")
    @JsonManagedReference
    private List<EventVenueAreaEntity> venueAreas;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "eventGroupDetail", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of event group detail tickets for this event", example = "[]")
    private List<TicketEntity> tickets;
}
