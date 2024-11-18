package com.example.carsharingapp.controller;

import com.example.carsharingapp.dto.UpdateUserProfileRequestDto;
import com.example.carsharingapp.dto.UpdateUserRoleRequestDto;
import com.example.carsharingapp.dto.UserDto;
import com.example.carsharingapp.model.User;
import com.example.carsharingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users "
        + "authentication and profiles")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PatchMapping("/{id}/role")
    @Operation(summary = "Update users role", description = "Update the role of a specific users")
    public UserDto updateUserRole(
            @PathVariable @Positive Long id,
            @RequestBody @Valid UpdateUserRoleRequestDto roleRequestDto) {
        return userService.updateUserRole(id, roleRequestDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    @Operation(summary = "Get my profile info", description = "Retrieve profile information of "
            + "the authenticated users")
    public UserDto getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getCurrentUserProfile(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/me")
    @Operation(summary = "Update profile info", description = "Update profile information of "
            + "the authenticated users")
    public UserDto updateProfileInfo(
            @RequestBody @Valid UpdateUserProfileRequestDto userProfileRequestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.updateUserProfile(userProfileRequestDto, user.getId());
    }
}
