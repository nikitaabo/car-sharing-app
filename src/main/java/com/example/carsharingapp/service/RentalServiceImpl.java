package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CreateRentalRequestDto;
import com.example.carsharingapp.dto.RentalDto;
import com.example.carsharingapp.exception.RentalException;
import com.example.carsharingapp.exception.ReturnDateException;
import com.example.carsharingapp.mapper.RentalMapper;
import com.example.carsharingapp.model.Car;
import com.example.carsharingapp.model.Rental;
import com.example.carsharingapp.model.User;
import com.example.carsharingapp.repository.CarRepository;
import com.example.carsharingapp.repository.UserRepository;
import com.example.carsharingapp.repository.rental.RentalRepository;
import com.example.carsharingapp.repository.rental.RentalSpecification;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public RentalDto addRental(CreateRentalRequestDto rentalRequestDto, Long userId) {
        Car car = carRepository.findById(rentalRequestDto.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found with ID: "
                        + rentalRequestDto.getCarId()));

        if (car.getInventory() <= 0) {
            throw new RentalException("Car is not available for rentals");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: "
                        + userId));

        Rental rental = rentalMapper.toModel(rentalRequestDto);
        rental.setCar(car);
        rental.setUser(user);

        car.setInventory(car.getInventory() - 1);
        carRepository.save(car);
        Rental savedRental = rentalRepository.save(rental);

        String message = String.format("New rentals created:\nUser ID: %d\nCar ID: %d"
                        + "\nRental Date: %s\nReturn Date: %s",
                userId, rentalRequestDto.getCarId(),
                rentalRequestDto.getRentalDate(), rentalRequestDto.getReturnDate());
        log.info(message);
        sendNotificationAsync(message);

        return rentalMapper.toDto(savedRental);
    }

    @Async
    public void sendNotificationAsync(String message) {
        notificationService.sendNotification(message);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDto> findRentals(Long userId, boolean isActive) {
        Specification<Rental> spec = RentalSpecification.byUserAndStatus(userId, isActive);
        return rentalRepository.findAll(spec).stream()
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RentalDto findRentalById(Long id) {
        Rental rental = rentalRepository.findByIdWithCar(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with ID: " + id));
        return rentalMapper.toDto(rental);
    }

    @Override
    @Transactional
    public RentalDto setActualReturnDate(Long id) {
        Rental rental = rentalRepository.findByIdWithCar(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with ID: " + id));

        if (!rental.isActive()) {
            throw new ReturnDateException("Rental is already completed");
        }

        rental.setActualReturnDate(LocalDate.now());
        rental.setActive(false);

        Car car = rental.getCar();
        car.setInventory(car.getInventory() + 1);
        carRepository.save(car);

        Rental updatedRental = rentalRepository.save(rental);
        log.info("Actual return date of rentals with id {} is set, return date is {}",
                id, rental.getReturnDate());
        return rentalMapper.toDto(updatedRental);
    }
}
