package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.EventGroupDetailsWithSeatMapResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Full event response with venue area details (e.g., Available seats)")
public class FullEventGroupResponse extends BaseEventGroupResponse {
    @Schema(description = "Venue details", example = "Sports Hall 1")
    private BaseVenueResponse venue;

    @Schema(description = "Event Group Details", example = "[]")
    private List<EventGroupDetailsWithSeatMapResponse> eventGroupDetails;
}
