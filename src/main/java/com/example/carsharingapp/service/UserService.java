package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.UserRegistrationRequestDto;
import com.example.carsharingapp.dto.UserResponseDto;
import com.example.carsharingapp.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException;
}
