package com.oop.ticketmasterswiftiebackend.user.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "xiaoboi98")
    private String username;

    @Email(message = "Email should be valid")
    @Schema(description = "User email", example = "kanielwzh1@gmail.com")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Schema(description = "User's mobile phone number", example = "+65 99999999")
    private String phoneNumber;

    @NotNull(message = "RoleID is required")
    @Schema(description = "User's role id", example = "1")
    private Integer roleId;
}
