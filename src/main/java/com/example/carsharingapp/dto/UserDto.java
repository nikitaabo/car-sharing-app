package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.User;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private User.UserRole role;
}

