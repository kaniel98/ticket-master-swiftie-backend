package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse.BaseEventGroupDetailsResponse;
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
public class CreateUpdateEventGroupResponse extends BaseEventGroupResponse {
    @Schema(description = "Venue Id", example = "123")
    private Integer venueId;

    @Schema(description = "Event Group Details", example = "[]")
    private List<BaseEventGroupDetailsResponse> eventGroupDetails;
}
