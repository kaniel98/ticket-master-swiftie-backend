package com.oop.ticketmasterswiftiebackend.eventStatistics.models.response;

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
@SuperBuilder
@Data
@Schema(name = "FullEventStatisticResponse", description = "Full response for event statistic")
public class FullEventStatisticResponse extends BaseEventStatisticResponse {
    @Schema(description = "Event category statistic", example = "EventCategoryStatisticEntity")
    private List<BaseEventCategoryStatisticResponse> eventCategoryStatistics;
}
