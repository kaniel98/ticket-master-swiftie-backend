package com.oop.ticketmasterswiftiebackend.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VerifyTicketValidityRequest {
    private Integer eventGroupDetailId;
    private Integer ticketId;
}
