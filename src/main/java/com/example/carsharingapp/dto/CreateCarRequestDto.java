package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.Car;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateCarRequestDto {
    @NotBlank(message = "Model is required")
    private String model;
    @NotBlank(message = "Brand is required")
    private String brand;
    @NotNull(message = "Car type is required")
    private Car.CarType type;
    @NotNull(message = "Inventory is required")
    @Positive(message = "Inventory must be positive")
    private int inventory;
    @NotNull(message = "Daily fee is required")
    @Positive(message = "Daily fee must be positive")
    private BigDecimal dailyFee;
}
