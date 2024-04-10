package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse;

import com.oop.ticketmasterswiftiebackend.event.constants.EventStatus;
import com.oop.ticketmasterswiftiebackend.event.constants.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Base event response without additional details")
public class BaseEventGroupResponse {
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

    @Schema(description = "Event description", example = "Taylor swift come sing her new album!")
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
}
