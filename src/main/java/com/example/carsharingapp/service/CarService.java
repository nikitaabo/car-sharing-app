package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CarDto;
import com.example.carsharingapp.dto.CreateCarRequestDto;
import com.example.carsharingapp.dto.InventoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CarService {
    CarDto save(CreateCarRequestDto carRequestDto);

    List<CarDto> findAll(Pageable pageable);

    CarDto findById(Long id);

    void deleteById(Long id);

    CarDto update(Long id, CreateCarRequestDto carRequestDto);

    CarDto updateCar(Long id, InventoryDto newInventory);
}
