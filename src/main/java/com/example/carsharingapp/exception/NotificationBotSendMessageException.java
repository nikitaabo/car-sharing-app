package com.example.carsharingapp.exception;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class NotificationBotSendMessageException extends RuntimeException {
    public NotificationBotSendMessageException(String message, TelegramApiException cause) {
        super(message, cause);
    }
}
