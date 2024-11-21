package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.Car;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CarDto {
    private Long id;
    private String model;
    private String brand;
    private Car.CarType type;
    private int inventory;
    private BigDecimal dailyFee;
}
