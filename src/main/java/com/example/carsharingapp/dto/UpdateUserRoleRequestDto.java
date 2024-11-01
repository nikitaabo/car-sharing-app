package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRoleRequestDto {
    @NotNull(message = "Role is required")
    private UserRole role;

}
