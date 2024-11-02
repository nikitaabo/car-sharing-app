package com.example.carsharingapp.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateRentalRequestDto {
    @NotNull(message = "Rental date is required")
    private LocalDate rentalDate;

    @NotNull(message = "Return date is required")
    private LocalDate returnDate;

    @NotNull(message = "Car ID is required")
    private Long carId;
}
