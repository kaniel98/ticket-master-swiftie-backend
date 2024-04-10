package com.oop.ticketmasterswiftiebackend.user.models.entities;

import com.oop.ticketmasterswiftiebackend.user.constants.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "`Role`")
public class RoleEntity {
    @Id
    @Schema(description = "Unique identifier for user account", example = "123")
    private Integer roleId;

    @Schema(description = "Role name", example = "ADMIN")
    @Enumerated(EnumType.STRING)
    private RoleName role;
}
