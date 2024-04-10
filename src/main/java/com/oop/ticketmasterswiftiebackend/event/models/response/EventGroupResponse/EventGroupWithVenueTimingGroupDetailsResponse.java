package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.BaseEventGroupDetailsResponse;
import com.oop.ticketmasterswiftiebackend.venue.models.response.BaseVenueResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Event Group With Venue Timing Group Details Response - Designated to be used for event group information response (With venue, timing  and event group details)")
public class EventGroupWithVenueTimingGroupDetailsResponse extends BaseEventGroupResponse {
    @Schema(description = "Venue details")
    private BaseVenueResponse venue;

    @Schema(description = "Timings for given eventGroup")
    @JsonIgnore
    private List<BaseEventGroupDetailsResponse> eventGroupDetails;

    @Schema(description = "Timings for this event group")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> timing;

    @Schema(description = "Timings for the given event group")
    public List<LocalDateTime> retrieveAndAddTiming() {
        List<LocalDateTime> result = new ArrayList<>();
        for (BaseEventGroupDetailsResponse eventGroupDetail : eventGroupDetails) {
            result.add(eventGroupDetail.getDateTime());
        }
        Collections.sort(result);
        return result;
    }
}
