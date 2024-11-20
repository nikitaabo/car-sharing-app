package com.example.carsharingapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingapp.dto.CreateRentalRequestDto;
import com.example.carsharingapp.dto.RentalDto;
import com.example.carsharingapp.exception.RentalException;
import com.example.carsharingapp.exception.ReturnDateException;
import com.example.carsharingapp.mapper.RentalMapper;
import com.example.carsharingapp.model.Car;
import com.example.carsharingapp.model.Rental;
import com.example.carsharingapp.model.User;
import com.example.carsharingapp.repository.CarRepository;
import com.example.carsharingapp.repository.RentalRepository;
import com.example.carsharingapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    @DisplayName("Save new rentals and return its DTO")
    void addRental_WhenCarAndUserExist_ShouldSaveRental() {
        // Given
        final Long userId = 1L;
        Long carId = 2L;
        CreateRentalRequestDto rentalRequestDto = new CreateRentalRequestDto();
        rentalRequestDto.setCarId(carId);
        rentalRequestDto.setRentalDate(LocalDate.now());
        rentalRequestDto.setReturnDate(LocalDate.now().plusDays(3));

        Car car = new Car();
        car.setId(carId);
        car.setInventory(1);

        User user = new User();
        user.setId(userId);

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setUser(user);

        RentalDto rentalDto = new RentalDto();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(rentalMapper.toModel(rentalRequestDto)).thenReturn(rental);
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toDto(rental)).thenReturn(rentalDto);

        // When
        RentalDto result = rentalService.addRental(rentalRequestDto, userId);

        // Then
        assertNotNull(result);
        verify(carRepository, times(1)).save(car);
        verify(notificationService, times(1)).sendNotification(anyString());
    }

    @Test
    @DisplayName("Should throw exception when car is not available")
    void addRental_WhenCarNotAvailable_ShouldThrowRentalException() {
        // Given
        Long carId = 2L;
        CreateRentalRequestDto rentalRequestDto = new CreateRentalRequestDto();
        rentalRequestDto.setCarId(carId);

        Car car = new Car();
        car.setId(carId);
        car.setInventory(0);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // When / Then
        assertThrows(RentalException.class, () -> rentalService.addRental(rentalRequestDto, 1L));
    }

    @Test
    @DisplayName("Find a rentals by id and return its DTO")
    void findRentalById_WhenRentalExists_ShouldReturnRentalDto() {
        // Given
        Long rentalId = 1L;
        Rental rental = new Rental();
        RentalDto rentalDto = new RentalDto();

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(rentalMapper.toDto(rental)).thenReturn(rentalDto);

        // When
        RentalDto result = rentalService.findRentalById(rentalId);

        // Then
        assertNotNull(result);
        verify(rentalRepository, times(1)).findById(rentalId);
    }

    @Test
    @DisplayName("Should throw exception when rentals is not found")
    void findRentalById_WhenRentalNotFound_ShouldThrowEntityNotFoundException() {
        // Given
        Long rentalId = 1L;

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> rentalService.findRentalById(rentalId));
    }

    @Test
    @DisplayName("Set actual return date")
    void setActualReturnDate_WhenRentalIsActive_ShouldUpdateReturnDate() {
        // Given
        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setIsActive(true);
        rental.setCar(new Car());

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental)).thenReturn(rental);

        RentalDto rentalDto = new RentalDto();
        when(rentalMapper.toDto(rental)).thenReturn(rentalDto);

        // When
        RentalDto result = rentalService.setActualReturnDate(rentalId);

        // Then
        assertNotNull(result);
        assertFalse(rental.getIsActive());
        assertNotNull(rental.getActualReturnDate());
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    @DisplayName("Should throw exception when rentals is not active")
    void setActualReturnDate_WhenRentalIsNotActive_ShouldThrowReturnDateException() {
        // Given
        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setIsActive(false);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        // When / Then
        assertThrows(ReturnDateException.class, () -> rentalService.setActualReturnDate(rentalId));
    }

    @Test
    @DisplayName("Find rentals by status")
    void findRentalsByStatus_ShouldReturnListOfRentals_WhenStatusIsProvided() {
        // Given
        final boolean isActive = true;

        Rental rental1 = new Rental();
        rental1.setId(1L);
        rental1.setIsActive(true);

        Rental rental2 = new Rental();
        rental2.setId(2L);
        rental2.setIsActive(true);

        List<Rental> rentals = List.of(rental1, rental2);

        RentalDto rentalDto1 = new RentalDto();
        rentalDto1.setId(1L);

        RentalDto rentalDto2 = new RentalDto();
        rentalDto2.setId(2L);

        when(rentalRepository.findByIsActive(isActive)).thenReturn(rentals);
        when(rentalMapper.toDto(rental1)).thenReturn(rentalDto1);
        when(rentalMapper.toDto(rental2)).thenReturn(rentalDto2);

        // When
        List<RentalDto> result = rentalService.findRentalsByStatus(isActive);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(rentalDto1.getId(), result.get(0).getId());
        assertEquals(rentalDto2.getId(), result.get(1).getId());
        verify(rentalRepository, times(1)).findByIsActive(isActive);
        verify(rentalMapper, times(2)).toDto(any(Rental.class));
    }

}
