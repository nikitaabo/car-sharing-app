package com.example.carsharingapp.service;

import com.example.carsharingapp.dto.PaymentSessionDto;
import com.example.carsharingapp.exception.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StripeService {
    private static final String SUCCESS_URL = "http://localhost:8080/payments/success?sessionId={CHECKOUT_SESSION_ID}";
    private static final String CANCEL_URL = "http://localhost:8080/payments/cancel?sessionId={CHECKOUT_SESSION_ID}";
    private static final String CURRENCY = "USD";
    private static final String STATUS_PAID = "paid";
    private static final BigDecimal CENTS_AMOUNT = BigDecimal.valueOf(100);
    private static final Long DEFAULT_QUANTITY = 1L;
    private static final int EXPIRATION_TIME_IN_MINUTES = 30;
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createStripeSession(
            PaymentSessionDto requestDto) {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setExpiresAt(getExpirationTime())
                        .setSuccessUrl(SUCCESS_URL)
                        .setCancelUrl(CANCEL_URL)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(DEFAULT_QUANTITY)
                                        .setPriceData(SessionCreateParams.LineItem.PriceData
                                                .builder()
                                                .setCurrency(CURRENCY)
                                                .setUnitAmountDecimal(
                                                        requestDto.price().multiply(CENTS_AMOUNT)
                                                )
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData
                                                                .ProductData.builder()
                                                                .setName(requestDto.name())
                                                                .setDescription(
                                                                        requestDto.description())
                                                                .build()
                                                )
                                                .build())
                                        .build())
                        .build();
        try {
            return Session.create(params);
        } catch (StripeException ex) {
            log.error("Error during Stripe session creation: ", ex);
            throw new PaymentException("Cant create stripe session");
        }
    }

    private long getExpirationTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plusMinutes(EXPIRATION_TIME_IN_MINUTES);
        return expirationTime.toEpochSecond(ZoneOffset.UTC);
    }
}
