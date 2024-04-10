package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse;

import com.oop.ticketmasterswiftiebackend.event.models.response.BaseEventGroupPricingResponse;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.BaseEventGroupDetailsResponse;
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
@Schema(description = "Event Group Information Response - Designated to be used for event group information response (Without seat map details)")
public class EventGroupInformationResponse extends BaseEventGroupResponse {
    @Schema(description = "Venue details", example = "Sports Hall 1")
    private BaseVenueResponse venue;

    @Schema(description = "Event Group Pricing Details", example = "[]")
    private List<BaseEventGroupPricingResponse> eventGroupPricing;

    @Schema(description = "Timings for given eventGroup")
    private List<BaseEventGroupDetailsResponse> eventGroupDetails;
}
