package com.oop.ticketmasterswiftiebackend.event.models.response;

import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BaseEventGroupPricingResponse {
    @Schema(description = "Venue area category", example = "VIP")
    @Enumerated(EnumType.STRING)
    private VenueAreaCategory category;

    @Schema(description = "Price for the given category", example = "100.00")
    private Double price;
}
