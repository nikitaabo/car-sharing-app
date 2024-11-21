package com.example.carsharingapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingapp.dto.UpdateUserProfileRequestDto;
import com.example.carsharingapp.dto.UpdateUserRoleRequestDto;
import com.example.carsharingapp.dto.UserDto;
import com.example.carsharingapp.dto.UserRegistrationRequestDto;
import com.example.carsharingapp.dto.UserResponseDto;
import com.example.carsharingapp.exception.RegistrationException;
import com.example.carsharingapp.mapper.UserMapper;
import com.example.carsharingapp.model.User;
import com.example.carsharingapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register a new users")
    public void register_WhenEmailIsUnique_ShouldSaveNewUser() {
        // Given
        UserRegistrationRequestDto registrationRequestDto = new UserRegistrationRequestDto();
        registrationRequestDto.setEmail("test@example.com");
        registrationRequestDto.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setEmail(registrationRequestDto.getEmail());
        user.setPassword("encodedPassword");
        user.setRole(User.UserRole.CUSTOMER);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);

        when(userRepository.existsByEmail(registrationRequestDto.getEmail())).thenReturn(false);
        when(userMapper.toModel(registrationRequestDto)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        // When
        UserResponseDto result = null;
        try {
            result = userService.register(registrationRequestDto);
        } catch (RegistrationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        // Then
        assertNotNull(result);
        assertEquals(userResponseDto.getId(), result.getId());
        verify(userRepository, times(1)).existsByEmail(registrationRequestDto.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    @DisplayName("Update users role")
    public void updateUserRole_WhenUserExists_ShouldUpdateRole() {
        // Given
        Long userId = 1L;
        UpdateUserRoleRequestDto roleRequestDto = new UpdateUserRoleRequestDto();
        roleRequestDto.setRole(User.UserRole.MANAGER);

        User user = new User();
        user.setId(userId);
        user.setRole(User.UserRole.CUSTOMER);

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setRole(User.UserRole.MANAGER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        // When
        UserDto result = userService.updateUserRole(userId, roleRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(User.UserRole.MANAGER, result.getRole());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toUserDto(user);
    }

    @Test
    @DisplayName("Get an users's profile")
    public void getCurrentUserProfile_WhenUserExists_ShouldReturnUserProfile() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");

        when(userMapper.toUserDto(user)).thenReturn(userDto);

        // When
        UserDto result = userService.getCurrentUserProfile(user);

        // Then
        assertNotNull(result);
        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userMapper, times(1)).toUserDto(user);
    }

    @Test
    @DisplayName("Update users profile")
    public void updateUserProfile_WhenUserExists_ShouldUpdateUserProfile() {
        // Given
        final Long userId = 1L;
        UpdateUserProfileRequestDto userProfileRequestDto = new UpdateUserProfileRequestDto();
        userProfileRequestDto.setFirstName("John");
        userProfileRequestDto.setLastName("Doe");
        userProfileRequestDto.setEmail("updated@example.com");

        User user = new User();
        user.setId(userId);
        user.setFirstName("OldName");
        user.setLastName("OldLastName");
        user.setEmail("old@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setFirstName(userProfileRequestDto.getFirstName());
        userDto.setLastName(userProfileRequestDto.getLastName());
        userDto.setEmail(userProfileRequestDto.getEmail());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        // When
        UserDto result = userService.updateUserProfile(userProfileRequestDto, userId);

        // Then
        assertNotNull(result);
        assertEquals(userProfileRequestDto.getFirstName(), result.getFirstName());
        assertEquals(userProfileRequestDto.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toUserDto(user);
    }
}
