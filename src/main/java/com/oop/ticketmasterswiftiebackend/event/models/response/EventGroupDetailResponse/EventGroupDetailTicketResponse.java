package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.EventGroupTicketResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Event Group Detail Response to be shown for ticket")
public class EventGroupDetailTicketResponse extends BaseEventGroupDetailsResponse {
    @Schema(description = "Event group information for ticket")
    private EventGroupTicketResponse eventGroup;
}
