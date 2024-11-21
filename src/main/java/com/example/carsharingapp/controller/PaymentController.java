package com.example.carsharingapp.controller;

import com.example.carsharingapp.dto.CreateSessionDto;
import com.example.carsharingapp.dto.PaymentDto;
import com.example.carsharingapp.model.User;
import com.example.carsharingapp.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Payment management", description = "Endpoints for managing payments")
@Validated
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    @ResponseBody
    @Operation(summary = "Get payments", description = "Get users's payments")
    public List<PaymentDto> getPayments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return paymentService.getPayments(user.getId());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    @ResponseBody
    @Operation(summary = "Create a payment session",
            description = "Create a payment session to work with Stripe")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createSession(@Valid @RequestBody CreateSessionDto paymentRequest) {
        return paymentService.createPaymentSession(paymentRequest);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(value = "/success")
    @Operation(summary = "Stripe redirection for successful payments",
            description = "Check successful Stripe payments")
    public String checkSuccess(@RequestParam(name = "sessionId") String sessionId) {
        paymentService.successPayment(sessionId);
        return "success";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cancel")
    @Operation(summary = "Stripe redirection for canceled payments",
            description = "Return payment paused message")
    public String checkCancel(@RequestParam(name = "sessionId") String sessionId) {
        paymentService.cancelPayment(sessionId);
        return "cancel";
    }
}
