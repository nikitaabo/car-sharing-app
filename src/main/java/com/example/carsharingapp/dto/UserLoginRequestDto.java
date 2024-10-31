package com.example.carsharingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        @Size(min = 8, max = 25)
        String email,
        @NotBlank
        @Size(min = 8, max = 25)
        String password
) {
}
