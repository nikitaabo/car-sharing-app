package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.CreateSessionDto;
import com.example.carsharingapp.dto.PaymentDto;
import java.util.List;

public interface PaymentService {
    List<PaymentDto> getPayments(Long userId);

    PaymentDto createPaymentSession(CreateSessionDto paymentRequest);

    void successPayment(String sessionId);

    void cancelPayment(String sessionId);
}
