package com.example.carsharingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationBot notificationBot;

    @Override
    public void sendNotification(String message) {
        notificationBot.sendMessage(message);
    }
}
