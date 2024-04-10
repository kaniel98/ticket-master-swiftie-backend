package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse;

import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseEventVenueAreaResponse;
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
@Schema(description = "Event Group Details Response with seat map details")
public class EventGroupDetailsWithSeatMapResponse extends BaseEventGroupDetailsResponse {
    @Schema(description = "List of event venue areas", example = "[]")
    private List<BaseEventVenueAreaResponse> venueAreas;
}
