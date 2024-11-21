package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.Payment;
import java.net.URL;
import lombok.Data;

@Data
public class PaymentDto {
    private Long id;
    private Payment.PaymentStatus status;
    private Payment.PaymentType type;
    private URL sessionUrl;
    private String sessionId;
}
