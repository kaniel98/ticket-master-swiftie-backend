package com.oop.ticketmasterswiftiebackend.user.models.response;

import com.oop.ticketmasterswiftiebackend.user.constants.AccountStatus;
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
public class UserResponse {
    @Schema(description = "Unique identifier for user account", example = "123")
    private Integer userId;
    @Schema(description = "Username", example = "xiaoboi98")
    private String username;
    @Schema(description = "User email", example = "kanielwzh1@gmail.com")
    private String email;
    @Schema(description = "User's mobile phone number", example = "+65 99999999")
    private String phoneNumber;
    @Schema(description = "User's account status", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}
