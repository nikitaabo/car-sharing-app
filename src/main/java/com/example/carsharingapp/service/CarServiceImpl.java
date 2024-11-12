package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CarDto;
import com.example.carsharingapp.dto.CreateCarRequestDto;
import com.example.carsharingapp.dto.InventoryDto;
import com.example.carsharingapp.exception.EntityNotFoundException;
import com.example.carsharingapp.mapper.CarMapper;
import com.example.carsharingapp.model.Car;
import com.example.carsharingapp.repository.car.CarRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto save(CreateCarRequestDto carRequestDto) {
        Car car = carMapper.toModel(carRequestDto);
        logger.info("New car with id {} is created.", car.getId());
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public List<CarDto> findAll(Pageable pageable) {
        return carRepository.findAll(pageable).stream()
                .map(carMapper::toDto).toList();
    }

    @Override
    public CarDto findById(Long id) {
        Car book = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find car by id: " + id));
        return carMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(id);
        logger.info("Car with id {} was deleted.", id);
    }

    @Override
    public CarDto update(Long id, CreateCarRequestDto carRequestDto) {
        Car car = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There's not car with id: " + id));
        carMapper.updateBookFromDto(carRequestDto, car);
        logger.info("Car with id {} is updated.", id);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public CarDto updateCar(Long id, InventoryDto newInventory) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        car.setInventory(newInventory.inventory());
        logger.info("Inventory of car with id {} is updated.", id);
        return carMapper.toDto(carRepository.save(car));
    }
}
