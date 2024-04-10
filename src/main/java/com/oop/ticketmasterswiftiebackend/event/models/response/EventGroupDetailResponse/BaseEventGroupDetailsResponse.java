package com.oop.ticketmasterswiftiebackend.event.models.response.EventGroupDetailResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class BaseEventGroupDetailsResponse {
    @Schema(description = "Unique identifier for the event group detail", example = "123")
    private Integer eventGroupDetailId;

    @Schema(description = "Event date time", example = "2022-12-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
}
