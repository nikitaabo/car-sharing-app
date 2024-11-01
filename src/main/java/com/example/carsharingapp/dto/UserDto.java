package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
}

