package com.example.carsharingapp.dto;

import java.math.BigDecimal;

public record PaymentSessionDto(BigDecimal price, String name, String description) {
}
