package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CreateRentalRequestDto;
import com.example.carsharingapp.dto.RentalDto;
import java.util.List;

public interface RentalService {
    RentalDto addRental(CreateRentalRequestDto rentalRequestDto, Long userId);

    List<RentalDto> findRentalsByUserAndStatus(Long userId, boolean isActive);

    RentalDto findRentalById(Long id);

    RentalDto setActualReturnDate(Long id);
}
