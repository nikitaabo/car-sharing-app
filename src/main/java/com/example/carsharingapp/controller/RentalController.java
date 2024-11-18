package com.example.carsharingapp.controller;

import com.example.carsharingapp.dto.CreateRentalRequestDto;
import com.example.carsharingapp.dto.RentalDto;
import com.example.carsharingapp.model.User;
import com.example.carsharingapp.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rental management", description = "Endpoints for managing rentals")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/rentals")
public class RentalController {

    private final RentalService rentalService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    @Operation(summary = "Create a rentals", description = "Create a new rentals")
    public RentalDto createRental(@RequestBody @Valid CreateRentalRequestDto rentalRequestDto,
                                  Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return rentalService.addRental(rentalRequestDto, user.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "Get rentals", description = "Get rentals by users ID and whether "
            + "the rentals is still active or not")
    public List<RentalDto> getRentalsByUserAndStatus(
            @RequestParam(name = "user_id", required = false) @Positive Long userId,
            @RequestParam(name = "is_active") boolean isActive,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.hasRoleManager()) {
            if (userId != null) {
                return rentalService.findRentalsByUserAndStatus(userId, isActive);
            } else {
                return rentalService.findRentalsByStatus(isActive);
            }
        } else {
            return rentalService.findRentalsByUserAndStatus(currentUser.getId(), isActive);
        }
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a rentals", description = "Get a rentals by id")
    public RentalDto getRentalById(@PathVariable @Positive Long id) {
        return rentalService.findRentalById(id);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/{id}/return")
    @Operation(summary = "Set actual return date", description = "Set actual return date "
            + "(increase car inventory by 1)")
    public RentalDto returnRental(@PathVariable @Positive Long id) {
        return rentalService.setActualReturnDate(id);
    }
}
