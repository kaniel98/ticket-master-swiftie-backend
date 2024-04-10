package com.oop.ticketmasterswiftiebackend.ticket.models.request;

import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketBookingMethod;
import com.oop.ticketmasterswiftiebackend.ticket.constants.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingTicketRequest {

    @NotNull(message = "Event ID is required")
    @Schema(description = "Ticket's Event ID", example = "123")
    private Integer eventGroupDetailId;

    @NotNull(message = "Event Venue Area id is required to book the tickets")
    @Schema(description = "Ticket's Event Venue Area ID", example = "123")
    private Integer eventVenueAreaId;

    @NotNull(message = "Customer ID is required")
    @Schema(description = "Ticket's Customer ID", example = "001")
    private Integer customerId;

    @NotNull(message = "Ticket's Status is required: ACTIVE, EXPIRED, CANCELLED_BY_ORGANISER, CANCELLED_BY_CUSTOMER")
    @Schema(description = "Ticket's Status", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @NotNull(message = "Ticket's Booking Method is required: ONSITE | ONLINE")
    @Schema(description = "Ticket's Booking Method:", example = "ONLINE")
    @Enumerated(EnumType.STRING)
    private TicketBookingMethod bookingMethod;

    @Schema(description = "Ticket's Number of Guest:", example = "3")
    @Min(value = 1, message = "Number of Guests should be between 1 and 4")
    @Max(value = 4, message = "Number of Guests should be between 1 and 4")
    private Integer numberOfGuests;
}
