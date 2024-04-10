package com.oop.ticketmasterswiftiebackend.venue.models.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oop.ticketmasterswiftiebackend.event.constants.VenueAreaCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Request body for creating/updating a venue area")
public class VenueAreaRequest {
    @Schema(description = "Number of columns", example = "3")
    @Min(value = 1, message = "Number of columns must be greater than 0")
    private Integer noOfCol;

    @Schema(description = "Number of rows", example = "3")
    @Min(value = 1, message = "Number of rows must be greater than 0")
    private Integer noOfRow;

    @Schema(description = "Category type", example = "CAT1")
    @NotNull(message = "Category type is required")
    private VenueAreaCategory category;

    @Schema(description = "Number of this category", example = "3")
    @Min(value = 1, message = "Number of category must be greater than 0")
    private Integer noOfCategory;

    @Schema(description = "Given position identification for each venue area with given category", example = "[CAT1_1, CAT1_2]")
    private String[] positions;

    @JsonIgnore
    @AssertTrue(message = "Number of positions must match the number of category")
    boolean isPositionsValid() {
        return positions.length == noOfCategory;
    }
}
