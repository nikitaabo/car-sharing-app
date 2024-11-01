package com.example.carsharingapp.dto;

import com.example.carsharingapp.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@FieldMatch(firstField = "password", secondField = "repeatedPassword")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatedPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
