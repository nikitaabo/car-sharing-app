package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CreateRentalRequestDto;
import com.example.carsharingapp.dto.RentalDto;
import java.util.List;

public interface RentalService {
    RentalDto addRental(CreateRentalRequestDto rentalRequestDto, Long userId);

    RentalDto findRentalById(Long id);

    RentalDto setActualReturnDate(Long id);

    List<RentalDto> findRentals(Long userId, boolean isActive);
}
