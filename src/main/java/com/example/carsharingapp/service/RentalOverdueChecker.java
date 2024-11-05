package com.example.carsharingapp.service;

import com.example.carsharingapp.model.Rental;
import com.example.carsharingapp.repository.rental.RentalRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalOverdueChecker {
    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkOverdueRentals() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Rental> overdueRentals = rentalRepository.findOverdueRentals(tomorrow);

        if (overdueRentals.isEmpty()) {
            notificationService.sendNotification("No rentals overdue today!");
        } else {
            overdueRentals.forEach(rental -> {
                String message = String.format(
                        "Overdue rental detected:\nUser ID: %d\nCar ID: %d"
                                + "\nRental Date: %s\nReturn Date: %s",
                        rental.getUser().getId(),
                        rental.getCar().getId(),
                        rental.getRentalDate(),
                        rental.getReturnDate()
                );
                notificationService.sendNotification(message);
            });
        }
    }
}
