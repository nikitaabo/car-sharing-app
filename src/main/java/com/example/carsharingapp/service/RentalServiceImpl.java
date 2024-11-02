package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CreateRentalRequestDto;
import com.example.carsharingapp.dto.RentalDto;
import com.example.carsharingapp.mapper.RentalMapper;
import com.example.carsharingapp.model.Car;
import com.example.carsharingapp.model.Rental;
import com.example.carsharingapp.model.User;
import com.example.carsharingapp.repository.car.CarRepository;
import com.example.carsharingapp.repository.rental.RentalRepository;
import com.example.carsharingapp.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;

    @Override
    @Transactional
    public RentalDto addRental(CreateRentalRequestDto rentalRequestDto, Long userId) {
        Car car = carRepository.findById(rentalRequestDto.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found with ID: "
                        + rentalRequestDto.getCarId()));

        if (car.getInventory() <= 0) {
            throw new IllegalStateException("Car is not available for rental");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: "
                        + userId));

        Rental rental = rentalMapper.toModel(rentalRequestDto);
        rental.setCar(car);
        rental.setUser(user);

        car.setInventory(car.getInventory() - 1);
        carRepository.save(car);

        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    @Override
    public List<RentalDto> findRentalsByUserAndStatus(Long userId, boolean isActive) {
        List<Rental> rentals = rentalRepository.findByUserIdAndIsActive(userId, isActive);
        return rentals.stream()
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentalDto> findRentalsByStatus(boolean isActive) {
        List<Rental> rentals = rentalRepository.findByIsActive(isActive);
        return rentals.stream()
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RentalDto findRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with ID: " + id));
        return rentalMapper.toDto(rental);
    }

    @Override
    @Transactional
    public RentalDto setActualReturnDate(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with ID: " + id));

        if (!rental.getIsActive()) {
            throw new IllegalStateException("Rental is already completed");
        }

        rental.setActualReturnDate(LocalDate.now());
        rental.setIsActive(false);

        Car car = rental.getCar();
        car.setInventory(car.getInventory() + 1);
        carRepository.save(car);

        Rental updatedRental = rentalRepository.save(rental);
        return rentalMapper.toDto(updatedRental);
    }
}
