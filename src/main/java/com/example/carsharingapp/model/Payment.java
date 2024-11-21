package com.example.carsharingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.net.URL;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "payments")
@Getter
@Setter
@SQLDelete(sql = "UPDATE payments SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction(value = "is_deleted = FALSE")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType type;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;
    @Column(name = "session_url", nullable = false)
    private URL sessionUrl;
    @Column(name = "session_id", nullable = false, unique = true)
    private String sessionId;
    @Column(name = "amount_to_pay", nullable = false)
    private BigDecimal amountToPay;
    private boolean isDeleted = false;

    public enum PaymentStatus {
        PENDING,
        PAID,
        CANCELED
    }

    public enum PaymentType {
        RENTAL_PAYMENT,
        LATE_RETURN_FINE
    }
}
