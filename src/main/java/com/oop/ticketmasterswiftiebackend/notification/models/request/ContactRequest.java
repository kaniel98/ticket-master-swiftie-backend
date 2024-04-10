package com.oop.ticketmasterswiftiebackend.notification.models.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContactRequest {
    
    @NotBlank(message = "email is required")
    @Schema(description = "email", example = "example@example.com")
    private String email;

}
