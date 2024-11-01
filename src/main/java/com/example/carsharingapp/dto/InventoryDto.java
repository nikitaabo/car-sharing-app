package com.example.carsharingapp.dto;

import jakarta.validation.constraints.NotNull;

public record InventoryDto(@NotNull int inventory) {
}
