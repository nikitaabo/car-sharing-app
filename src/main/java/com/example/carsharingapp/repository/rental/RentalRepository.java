package com.example.carsharingapp.repository.rental;

import com.example.carsharingapp.model.Rental;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserIdAndIsActive(Long userId, boolean isActive);

    List<Rental> findByIsActive(boolean isActive);

    @Query("SELECT r FROM Rental r WHERE r.returnDate <= :tomorrow AND r.isActive = true")
    List<Rental> findOverdueRentals(LocalDate tomorrow);
}