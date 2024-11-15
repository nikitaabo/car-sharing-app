package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.enums.Type;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSessionDto {
    @NotNull
    private Long rentalId;
    @NotNull
    private Type paymentType;
}
