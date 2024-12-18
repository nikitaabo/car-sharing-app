package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRoleRequestDto {
    @NotNull(message = "Role is required")
    private User.UserRole role;

}
