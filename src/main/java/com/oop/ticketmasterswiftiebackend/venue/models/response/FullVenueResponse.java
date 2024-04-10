package com.oop.ticketmasterswiftiebackend.venue.models.response;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.BaseEventGroupResponse;
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
@Schema(description = "Full venue response with venue area details and the events held here")
public class FullVenueResponse extends BaseVenueResponse {
    @Schema(description = "List of venue areas", example = "[]")
    private List<VenueAreaResponse> seatMap;

    @Schema(description = "List of events", example = "[]")
    private List<BaseEventGroupResponse> events;
}
