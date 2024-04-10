package com.oop.ticketmasterswiftiebackend.common.models;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BaseRequest<T> {
    @Valid
    private T data;
}