package com.example.carsharingapp.service;

import com.example.carsharingapp.exception.NotificationBotSendMessageException;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class NotificationBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botName;
    private final Set<String> activeChatIds = new HashSet<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = String.valueOf(update.getMessage().getChatId());
            addChatId(chatId);

            String messageText = update.getMessage().getText();
            if (messageText.equals("/start")) {
                sendOnStartCommand(update.getMessage().getChat().getFirstName());
            }
        }
    }

    private void addChatId(String chatId) {
        activeChatIds.add(chatId);
    }

    private void sendOnStartCommand(String name) {
        String message = """
                Hi, %s! 👋🏻 Welcome to this bot!
                                 
                In this bot, you'll receive notifications about:
                📌 Newly created rentals
                📌 Overdue rentals                                           
                Enjoy using this bot!
                """.formatted(name);
        log.info("Sending a message to {}, message: {}.", name, message);
        sendMessage(message);
    }

    public void sendMessage(String text) {
        String chatId = getChatId();
        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
            log.info("Message sent successfully (chat id - {}, message - {}.)", chatId, text);
        } catch (TelegramApiException e) {
            log.error("Sending of message failed (chat id - {}, message - {}.)", chatId, text);
            throw new NotificationBotSendMessageException("There was an error during "
                    + "sending a message: " + text, e);
        }
    }

    private String getChatId() {
        return activeChatIds.stream()
                .findFirst()
                .orElseThrow(null);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
