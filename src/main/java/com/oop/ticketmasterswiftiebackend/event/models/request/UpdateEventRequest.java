package com.oop.ticketmasterswiftiebackend.event.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oop.ticketmasterswiftiebackend.event.constants.EventType;
import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateEventRequest {

    @NotNull(message = "Event id is required")
    @Schema(description = "Unique identifier for an event", example = "123")
    private Integer eventGroupId;

    @NotBlank(message = "Event name is required")
    @Schema(description = "Event name", example = "Taylor Swift Concert")
    private String name;

    @NotNull(message = "Venue id is required")
    @Schema(description = "Venue id", example = "Id of the venue which the event is held")
    private Integer venueId;

    @NotNull(message = "Event Type is required")
    @Schema(description = "Event Type", example = "MUSIC_CONCERT")
    private EventType eventType;

    @NotEmpty(message = "Event date time is required")
    @Schema(description = "Event date time", example = "[2022-12-12 12:00:00]")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> timings;

    @NotNull(message = "Need to indicate if booking allowed")
    @Schema(description = "Indicate if event is open for booking", example = "true")
    private Boolean bookingAllowed;

    @Schema(description = "Cancellation fee", example = "10.00")
    private Double cancellationFee;

    @Schema(description = "Event description", example = "ACTIVE")
    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Event description is required")
    private String description;

    @NotNull(message = "Event Manager's ID is required")
    @Schema(description = "Event Manager's ID", example = "1")
    private Integer eventManagerId;

    @Schema(description = "Image URL for the event poster", example = "www.image.com")
    private String posterImgUrl;

    @Schema(description = "Image URL for the event banner", example = "www.image.com")
    private String bannerImgUrl;

    @Schema(description = "Prices for different categories", example = "{ \"CAT1\": 100.00, \"CAT2\": 50.00 }")
    private Map<VenueAreaCategory, Double> categoryPrices;
}
