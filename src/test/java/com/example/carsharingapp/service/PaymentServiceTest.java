package com.example.carsharingapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingapp.dto.CreateSessionDto;
import com.example.carsharingapp.dto.PaymentDto;
import com.example.carsharingapp.dto.PaymentSessionDto;
import com.example.carsharingapp.mapper.PaymentMapper;
import com.example.carsharingapp.model.Car;
import com.example.carsharingapp.model.Payment;
import com.example.carsharingapp.model.Rental;
import com.example.carsharingapp.model.enums.Status;
import com.example.carsharingapp.model.enums.Type;
import com.example.carsharingapp.repository.PaymentRepository;
import com.example.carsharingapp.repository.RentalRepository;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private StripeService stripeService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Get a list of users's payments")
    void getPayments_WhenPaymentsExist_ShouldReturnPaymentDtos() {
        // Given
        Long userId = 1L;
        List<Payment> payments = List.of(new Payment(), new Payment());
        final List<PaymentDto> paymentDtos = List.of(new PaymentDto(), new PaymentDto());

        when(paymentRepository.findAllByRentalUserId(userId)).thenReturn(payments);
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(new PaymentDto());

        // When
        List<PaymentDto> result = paymentService.getPayments(userId);

        // Then
        assertNotNull(result);
        assertEquals(paymentDtos.size(), result.size());
        verify(paymentRepository, times(1)).findAllByRentalUserId(userId);
        verify(paymentMapper, times(payments.size())).toDto(any(Payment.class));
    }

    @Test
    @DisplayName("Create a new payment")
    void createPaymentSession_WhenRentalExistsAndTypeFine_ShouldReturnPaymentDto() {
        // Given
        CreateSessionDto paymentRequest = new CreateSessionDto();
        paymentRequest.setRentalId(1L);
        paymentRequest.setPaymentType(Type.FINE);

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setCar(new Car());
        rental.getCar().setDailyFee(BigDecimal.valueOf(100));
        rental.getCar().setModel("Model X");
        rental.getCar().setBrand("Brand Y");
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(5));
        rental.setActualReturnDate(LocalDate.now().plusDays(8));

        Payment payment = new Payment();
        payment.setRental(rental);

        Session stripeSession = new Session();
        stripeSession.setId("session_id");
        stripeSession.setUrl("http://stripe.url");

        when(rentalRepository.findById(paymentRequest.getRentalId()))
                .thenReturn(Optional.of(rental));
        when(stripeService.createStripeSession(any(PaymentSessionDto.class)))
                .thenReturn(stripeSession);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(new PaymentDto());

        // When
        PaymentDto result = paymentService.createPaymentSession(paymentRequest);

        // Then
        assertNotNull(result);
        verify(rentalRepository, times(1)).findById(paymentRequest.getRentalId());
        verify(stripeService, times(1)).createStripeSession(any(PaymentSessionDto.class));
        verify(notificationService, times(1)).sendNotification(anyString());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Confirm a payment")
    void successPayment_WhenSessionIdExists_ShouldUpdateStatusToPaid() {
        // Given
        String sessionId = "session_id";
        Payment payment = new Payment();
        payment.setSessionId(sessionId);
        payment.setStatus(Status.PENDING);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(Optional.of(payment));

        // When
        paymentService.successPayment(sessionId);

        // Then
        assertEquals(Status.PAID, payment.getStatus());
        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verify(paymentRepository, times(1)).save(payment);
        verify(notificationService, times(1)).sendNotification(anyString());
    }

    @Test
    @DisplayName("Cancel a payment")
    void cancelPayment_WhenSessionIdExists_ShouldUpdateStatusToCanceled() {
        // Given
        String sessionId = "session_id";
        Payment payment = new Payment();
        payment.setSessionId(sessionId);
        payment.setStatus(Status.PENDING);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(Optional.of(payment));

        // When
        paymentService.cancelPayment(sessionId);

        // Then
        assertEquals(Status.CANCELED, payment.getStatus());
        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verify(paymentRepository, times(1)).save(payment);
        verify(notificationService, times(1)).sendNotification(anyString());
    }
}

