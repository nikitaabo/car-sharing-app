package com.example.carsharingapp.exception;

import com.stripe.exception.StripeException;

public class BookingException extends RuntimeException {
    public BookingException(String message, StripeException e) {
        super(message, e);
    }
}
