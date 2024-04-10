package com.oop.ticketmasterswiftiebackend.event.models.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateEventImageRequest {
    @NotNull(message = "Event group id is required")
    private Integer eventGroupId;
    private String bannerImgFileName;
    private String posterImgFileName;
}
