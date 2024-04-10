package com.oop.ticketmasterswiftiebackend.common.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BaseException extends RuntimeException {
    private Integer statusCode;
    private String businessCode;
    private String description;
}
