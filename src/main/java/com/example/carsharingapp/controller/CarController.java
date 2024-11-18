package com.example.carsharingapp.controller;

import com.example.carsharingapp.dto.CarDto;
import com.example.carsharingapp.dto.CreateCarRequestDto;
import com.example.carsharingapp.dto.InventoryDto;
import com.example.carsharingapp.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car management", description = "Endpoints for managing cars")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cars")
public class CarController {
    private final CarService carService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    @Operation(summary = "Create a car", description = "Create a new car")
    public CarDto createCar(@RequestBody @Valid CreateCarRequestDto carRequestDto) {
        return carService.save(carRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get all cars", description = "Get a list of available cars")
    public List<CarDto> getAll(Pageable pageable) {
        return carService.findAll(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Get a car", description = "Get a car by id")
    public CarDto getCarById(@PathVariable @Positive Long id) {
        return carService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a car", description = "Update a car by id")
    public CarDto updateCar(@PathVariable Long id,
            @Valid @RequestBody CreateCarRequestDto carRequestDto) {
        return carService.update(id, carRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update a car", description = "Update an inventory of car")
    public CarDto changeInventory(@PathVariable @Positive Long id,
                                 @RequestBody @Valid InventoryDto newInventory) {
        return carService.updateCar(id, newInventory);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car", description = "Delete a car by id")
    public void deleteCar(@PathVariable @Positive Long id) {
        carService.deleteById(id);
    }

}
