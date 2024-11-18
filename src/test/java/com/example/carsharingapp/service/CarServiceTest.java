package com.example.carsharingapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingapp.dto.CarDto;
import com.example.carsharingapp.dto.CreateCarRequestDto;
import com.example.carsharingapp.dto.InventoryDto;
import com.example.carsharingapp.exception.EntityNotFoundException;
import com.example.carsharingapp.mapper.CarMapper;
import com.example.carsharingapp.model.Car;
import com.example.carsharingapp.model.enums.CarType;
import com.example.carsharingapp.repository.car.CarRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;
    @InjectMocks
    private CarServiceImpl carService;

    @Test
    @DisplayName("Save new car and return its DTO")
    public void saveCar_ValidData_ShouldReturnSavedCarDto() {
        // Given
        CreateCarRequestDto carRequestDto = CarServiceTestUtil.getCarRequestDto("Tesla", "Model S",
                CarType.SEDAN, 5, BigDecimal.valueOf(50));

        Car car = CarServiceTestUtil.getCar("Tesla", "Model S",
                CarType.SEDAN, 5, BigDecimal.valueOf(50));
        CarDto carDto = CarServiceTestUtil.getCarDto(1L, "Tesla", "Model S",
                CarType.SEDAN, 5, BigDecimal.valueOf(50));

        when(carMapper.toModel(carRequestDto)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(carDto);

        // When
        CarDto result = carService.save(carRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(carDto.getId(), result.getId());
        assertEquals(carDto.getBrand(), result.getBrand());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    @DisplayName("Find all cars and return their DTOs")
    void findAllCars_ShouldReturnAllCarsDto() {
        // Given
        List<Car> cars = CarServiceTestUtil.getCars();
        List<CarDto> carDtos = CarServiceTestUtil.getCarDtos();
        Page<Car> pageCars = new PageImpl<>(cars);

        when(carRepository.findAll(Pageable.unpaged())).thenReturn(pageCars);
        when(carMapper.toDto(cars.get(0))).thenReturn(carDtos.get(0));
        when(carMapper.toDto(cars.get(1))).thenReturn(carDtos.get(1));

        // When
        List<CarDto> result = carService.findAll(Pageable.unpaged());

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(carDtos.get(0).getId(), result.get(0).getId());
        verify(carRepository, times(1)).findAll(Pageable.unpaged());
    }

    @Test
    @DisplayName("Find car by ID and return its DTO")
    void findById_ExistingCar_ShouldReturnCarDto() {
        // Given
        Car car = CarServiceTestUtil.getCar("Tesla", "Model S",
                CarType.SEDAN, 5, BigDecimal.valueOf(50));
        CarDto carDto = CarServiceTestUtil.getCarDto(1L, "Tesla", "Model S",
                CarType.SEDAN, 5, BigDecimal.valueOf(50));

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(carDto);

        // When
        CarDto result = carService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(carDto.getId(), result.getId());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find car by ID and throw exception if not found")
    void findById_NonExistingCar_ShouldThrowException() {
        // Given
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.findById(1L));

        assertEquals("Can't find car by id: 1", exception.getMessage());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Delete car by ID")
    void deleteById_ShouldDeleteCar() {
        // When
        carService.deleteById(1L);

        // Then
        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Update car inventory and return updated DTO")
    void updateCarInventory_ShouldReturnUpdatedCarDto() {
        // Given
        InventoryDto inventoryDto = new InventoryDto(15);
        Car car = CarServiceTestUtil.getCar("Tesla", "Model S",
                CarType.SEDAN, 5, BigDecimal.valueOf(50));
        Car updatedCar = CarServiceTestUtil.getCar("Tesla", "Model S",
                CarType.SEDAN, 15, BigDecimal.valueOf(50));
        CarDto carDto = CarServiceTestUtil.getCarDto(1L, "Tesla", "Model S",
                CarType.SEDAN, 15, BigDecimal.valueOf(50));

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(updatedCar);
        when(carMapper.toDto(updatedCar)).thenReturn(carDto);

        // When
        CarDto result = carService.updateCar(1L, inventoryDto);

        // Then
        assertNotNull(result);
        assertEquals(15, result.getInventory());
        verify(carRepository, times(1)).save(car);
    }
}
