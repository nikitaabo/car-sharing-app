package com.example.carsharingapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private String additionalMessage;
}

