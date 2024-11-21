package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CreateSessionDto;
import com.example.carsharingapp.dto.PaymentDto;
import com.example.carsharingapp.dto.PaymentSessionDto;
import com.example.carsharingapp.mapper.PaymentMapper;
import com.example.carsharingapp.model.Payment;
import com.example.carsharingapp.model.Rental;
import com.example.carsharingapp.repository.PaymentRepository;
import com.example.carsharingapp.repository.rental.RentalRepository;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private static final Double FINE_MULTIPLIER = 1.5;
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final PaymentMapper paymentMapper;
    private final StripeService stripeService;
    private final NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getPayments(Long userId) {
        return paymentRepository.findAllByRentalUserId(userId).stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDto createPaymentSession(CreateSessionDto paymentRequest) {
        Rental rental = rentalRepository.findById(paymentRequest.getRentalId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setType(paymentRequest.getPaymentType());
        BigDecimal amount = calculateAmount(rental, paymentRequest.getPaymentType());
        payment.setAmountToPay(amount);

        PaymentSessionDto paymentSessionDto = new PaymentSessionDto(
                amount, "Rental" + rental.getCar().getModel() + rental.getCar().getBrand(),
                "This is a session for car rentals payment");
        Session stripeSession = stripeService.createStripeSession(
                paymentSessionDto);
        payment.setSessionId(stripeSession.getId());
        try {
            payment.setSessionUrl(new URL(stripeSession.getUrl()));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid session URL: " + stripeSession.getUrl(), e);
        }
        String message = String.format("New payment created:\nType: %s\nRental ID: %d"
                        + "\nSession Id: %s\nStatus: %s",
                payment.getType(), payment.getRental().getId(),
                payment.getSessionId(), payment.getStatus());
        log.info(message);
        sendNotificationAsync(message);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public void successPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("There is no a payment with session id "
                        + sessionId));
        payment.setStatus(Payment.PaymentStatus.PAID);
        log.info("Payment with session id {} is successful.", sessionId);
        sendNotificationAsync("Payment with session id {} is successful.");
        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void cancelPayment(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("There is no a payment with session id "
                        + sessionId));
        payment.setStatus(Payment.PaymentStatus.CANCELED);
        log.info("Payment with session id {} is canceled.", sessionId);
        sendNotificationAsync("Payment with session id {} is canceled.");
        paymentRepository.save(payment);
    }

    @Async
    public void sendNotificationAsync(String message) {
        notificationService.sendNotification(message);
    }

    private BigDecimal calculateAmount(Rental rental, Payment.PaymentType paymentType) {
        BigDecimal dailyRate = rental.getCar().getDailyFee();
        long days = calculateDays(rental);
        BigDecimal amount = dailyRate.multiply(BigDecimal.valueOf(days));

        if (Payment.PaymentType.LATE_RETURN_FINE.equals(paymentType)) {
            long overdueDays = calculateOverdueDays(rental);
            BigDecimal fineMultiplier = new BigDecimal(FINE_MULTIPLIER);
            BigDecimal overdueCost = dailyRate.multiply(fineMultiplier)
                    .multiply(BigDecimal.valueOf(overdueDays));
            amount = amount.add(overdueCost);
        }
        return amount;
    }

    private long calculateDays(Rental rental) {
        LocalDate rentalDate = rental.getRentalDate();
        LocalDate returnDate = rental.getReturnDate();
        return ChronoUnit.DAYS.between(rentalDate, returnDate);
    }

    private long calculateOverdueDays(Rental rental) {
        LocalDate returnDate = rental.getReturnDate();
        LocalDate actualReturnDate = rental.getActualReturnDate();
        return ChronoUnit.DAYS.between(returnDate, actualReturnDate);
    }
}
