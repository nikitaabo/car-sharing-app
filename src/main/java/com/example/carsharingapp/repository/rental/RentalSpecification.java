package com.example.carsharingapp.repository.rental;

import com.example.carsharingapp.model.Rental;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class RentalSpecification {
    public static Specification<Rental> byUserAndStatus(Long userId, Boolean isActive) {
        return (Root<Rental> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (userId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("user").get("id"), userId));
            }

            if (isActive != null) {
                predicate = cb.and(predicate, cb.equal(root.get("isActive"), isActive));
            }
            return predicate;
        };
    }
}
