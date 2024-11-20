package com.example.carsharingapp.service;

import com.example.carsharingapp.model.Rental;
import com.example.carsharingapp.repository.RentalRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalOverdueChecker {
    private static final Logger logger = LoggerFactory.getLogger(RentalOverdueChecker.class);
    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkOverdueRentals() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Rental> overdueRentals = rentalRepository.findOverdueRentals(tomorrow);

        if (overdueRentals.isEmpty()) {
            logger.info("Sending a notification about no overdue rentals");
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
                logger.info("Sending a notification about overdue rentals");
                notificationService.sendNotification(message);
            });
        }
    }
}
