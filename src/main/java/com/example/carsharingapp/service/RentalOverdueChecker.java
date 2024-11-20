package com.example.carsharingapp.service;

import com.example.carsharingapp.model.Rental;
import com.example.carsharingapp.repository.RentalRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalOverdueChecker {
    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkOverdueRentals() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Rental> overdueRentals = rentalRepository.findOverdueRentals(tomorrow);

        if (overdueRentals.isEmpty()) {
            log.info("Sending a notification about no overdue rentals");
            notificationService.sendNotification("No rentals overdue today!");
        } else {
            overdueRentals.forEach(rental -> {
                String message = String.format(
                        "Overdue rentals detected:\nRental ID: %s\nUser ID: %d\nCar ID: %d"
                                + "\nRental Date: %s\nReturn Date: %s"
                                + "\nActual return date: %s",
                        rental.getId(),
                        rental.getUser().getId(),
                        rental.getCar().getId(),
                        rental.getRentalDate(),
                        rental.getReturnDate(),
                        rental.getActualReturnDate()
                );
                log.info("Sending a notification about overdue rentals");
                notificationService.sendNotification(message);
            });
        }
    }
}
