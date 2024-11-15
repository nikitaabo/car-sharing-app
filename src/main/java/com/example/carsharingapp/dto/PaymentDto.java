package com.example.carsharingapp.dto;

import com.example.carsharingapp.model.enums.Status;
import com.example.carsharingapp.model.enums.Type;
import java.net.URL;
import lombok.Data;

@Data
public class PaymentDto {
    private Long id;
    private Status status;
    private Type type;
    private URL sessionUrl;
    private String sessionId;
}
