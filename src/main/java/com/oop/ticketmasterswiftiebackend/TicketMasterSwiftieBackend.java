package com.oop.ticketmasterswiftiebackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@OpenAPIDefinition(info = @Info(title = "Ticket Master for Taylor Swift", version = "1.0", description = "BE for Ticket Master"))
public class TicketMasterSwiftieBackend {

    public static void main(String[] args) {
        SpringApplication.run(TicketMasterSwiftieBackend.class, args);
    }

    // Swagger documentation - http://localhost:3000/swagger-ui/index.html
}