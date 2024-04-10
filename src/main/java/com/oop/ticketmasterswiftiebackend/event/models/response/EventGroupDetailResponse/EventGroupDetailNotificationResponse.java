package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse;

import com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupResponse.EventGroupNotificationResponse;
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
public class EventGroupDetailNotificationResponse extends BaseEventGroupDetailsResponse {
    @Schema(description = "Event group information for notification")
    private EventGroupNotificationResponse eventGroup;
}
