package com.oop.ticketmasterswiftiebackend.user.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateUserRequest {
    @Schema(description = "Username", example = "xiaoboi98")
    private String username;

    @Schema(description = "User email", example = "kanielwzh1@gmail.com")
    private String email;

    @Schema(description = "User's mobile phone number", example = "+65 99999999")
    private String phoneNumber;
}
