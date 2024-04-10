package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.BaseEventGroupResponse;
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
@Schema(description = "Full Event Group Details Response - Venue details and event group information")
public class FullEventGroupDetailsResponse extends BaseEventGroupDetailsResponse {
    @Schema(description = "List of event venue areas", example = "[]")
    private List<BaseEventVenueAreaResponse> venueAreas;

    @Schema(description = "Event group information", example = "{}")
    private BaseEventGroupResponse eventGroup;
}
