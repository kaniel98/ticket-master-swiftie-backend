package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse;

import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventGroupNotificationResponse {
    @Schema(description = "Event name", example = "Taylor Swift Concert")
    private String name;

    @Schema(description = "Poster image", example = "")
    private String posterImgUrl;

    @Schema(description = "Base response for venue")
    private BaseVenueResponse venue;
}
