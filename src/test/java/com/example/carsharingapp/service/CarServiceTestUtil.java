package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CarDto;
import com.example.carsharingapp.dto.CreateCarRequestDto;
import com.example.carsharingapp.model.Car;
import com.example.carsharingapp.model.enums.CarType;
import java.math.BigDecimal;
import java.util.List;

public class CarServiceTestUtil {
    public static CreateCarRequestDto getCarRequestDto(String brand, String model, CarType type,
                                                       int inventory, BigDecimal dailyFee) {
        CreateCarRequestDto carRequestDto = new CreateCarRequestDto();
        carRequestDto.setBrand(brand);
        carRequestDto.setModel(model);
        carRequestDto.setType(type);
        carRequestDto.setInventory(inventory);
        carRequestDto.setDailyFee(dailyFee);
        return carRequestDto;
    }

    public static Car getCar(String brand, String model, CarType type,
                             int inventory, BigDecimal dailyFee) {
        Car car = new Car();
        car.setBrand(brand);
        car.setModel(model);
        car.setType(type);
        car.setInventory(inventory);
        car.setDailyFee(dailyFee);
        return car;
    }

    public static CarDto getCarDto(Long id, String brand, String model, CarType type,
                                   int inventory, BigDecimal dailyFee) {
        CarDto carDto = new CarDto();
        carDto.setId(id);
        carDto.setBrand(brand);
        carDto.setModel(model);
        carDto.setType(type);
        carDto.setInventory(inventory);
        carDto.setDailyFee(dailyFee);
        return carDto;
    }

    public static List<Car> getCars() {
        Car car1 = getCar("Tesla", "Model S",
                CarType.SEDAN, 5, BigDecimal.valueOf(50));
        Car car2 = getCar("Audi", "Q7",
                CarType.SEDAN, 10, BigDecimal.valueOf(60));
        List<Car> cars = List.of(car1, car2);
        return cars;
    }

    public static List<CarDto> getCarDtos() {
        CarDto carDto1 = getCarDto(1L, "Tesla", "Model S",
                CarType.SEDAN, 5, BigDecimal.valueOf(50));
        CarDto carDto2 = getCarDto(2L, "Audi", "Q7",
                CarType.SEDAN, 10, BigDecimal.valueOf(60));
        List<CarDto> carDtos = List.of(carDto1, carDto2);
        return carDtos;
    }
}
