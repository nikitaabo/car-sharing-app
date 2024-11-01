package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.UpdateUserProfileRequestDto;
import com.example.carsharingapp.dto.UpdateUserRoleRequestDto;
import com.example.carsharingapp.dto.UserDto;
import com.example.carsharingapp.dto.UserRegistrationRequestDto;
import com.example.carsharingapp.dto.UserResponseDto;
import com.example.carsharingapp.exception.RegistrationException;
import com.example.carsharingapp.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException;

    UserDto updateUserRole(Long id, UpdateUserRoleRequestDto roleRequestDto);

    UserDto getCurrentUserProfile(User user);

    UserDto updateUserProfile(UpdateUserProfileRequestDto userProfileRequestDto, Long id);
}
