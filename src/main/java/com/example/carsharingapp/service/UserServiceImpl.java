package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.UpdateUserProfileRequestDto;
import com.example.carsharingapp.dto.UpdateUserRoleRequestDto;
import com.example.carsharingapp.dto.UserDto;
import com.example.carsharingapp.dto.UserRegistrationRequestDto;
import com.example.carsharingapp.dto.UserResponseDto;
import com.example.carsharingapp.exception.EntityNotFoundException;
import com.example.carsharingapp.exception.RegistrationException;
import com.example.carsharingapp.mapper.UserMapper;
import com.example.carsharingapp.model.User;
import com.example.carsharingapp.model.enums.UserRole;
import com.example.carsharingapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RegistrationException("User with email " + registrationRequestDto.getEmail()
                    + " already exists");
        }
        User user = userMapper.toModel(registrationRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUserRole(Long id, UpdateUserRoleRequestDto roleRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole(roleRequestDto.getRole());
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getCurrentUserProfile(User user) {
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUserProfile(UpdateUserProfileRequestDto userProfileRequestDto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setFirstName(userProfileRequestDto.getFirstName());
        user.setLastName(userProfileRequestDto.getLastName());
        user.setEmail(userProfileRequestDto.getEmail());
        return userMapper.toUserDto(userRepository.save(user));
    }
}
