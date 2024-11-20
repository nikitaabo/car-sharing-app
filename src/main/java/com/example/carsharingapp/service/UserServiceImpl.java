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
import com.example.carsharingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
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
        logger.info("New users was registered with id {}", user.getId());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUserRole(Long id, UpdateUserRoleRequestDto roleRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole(roleRequestDto.getRole());
        logger.info("User's role with id {} was changed", user.getId());
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
        logger.info("User's profile with id {} was changed", user.getId());
        return userMapper.toUserDto(userRepository.save(user));
    }
}
