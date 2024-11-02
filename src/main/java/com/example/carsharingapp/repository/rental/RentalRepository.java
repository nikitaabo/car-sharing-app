package com.example.carsharingapp.repository.rental;

import com.example.carsharingapp.model.Rental;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserIdAndIsActive(Long userId, boolean isActive);
}
