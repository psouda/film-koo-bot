package com.psouda.filmkoobot.bot;

import com.psouda.filmkoobot.membership.MembershipChecker;
import com.psouda.filmkoobot.membership.TelegramMembershipChecker;
import com.psouda.filmkoobot.message.MessageSender;
import com.psouda.filmkoobot.message.TelegramMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FilmKooBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(FilmKooBot.class);

    private final MembershipChecker membershipChecker;
    private final MessageSender messageSender;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public FilmKooBot() {
        this.membershipChecker = new TelegramMembershipChecker(this);
        this.messageSender = new TelegramMessageSender(this);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            long chatId = getChatIdFromUpdate(update);
            String username = getUsernameFromUpdate(update);

            logUserRequest(username);

            if (isStartCommand(update)) {
                handleStartCommand(chatId);
            } else if (isRetryCallback(update)) {
                handleRetryCallback(chatId);
            }
        } catch (TelegramApiException e) {
            logger.error("An error occurred while processing update: {}", update, e);
        }
    }

    private long getChatIdFromUpdate(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return 0;
    }

    private String getUsernameFromUpdate(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getUserName();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getUserName();
        }
        return "Unknown User";
    }

    private void logUserRequest(String username) {
        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.info("Request received from user: {} at {}", username, formattedTime);
    }

    private boolean isStartCommand(Update update) {
        return update.hasMessage() && "/start".equals(update.getMessage().getText());
    }

    private boolean isRetryCallback(Update update) {
        return update.hasCallbackQuery() && "retry".equals(update.getCallbackQuery().getData());
    }

    private void handleStartCommand(long chatId) throws TelegramApiException {
        if (membershipChecker.checkUserMembership(chatId)) {
            messageSender.sendChannelLinks(chatId);
        } else {
            messageSender.sendMembershipRequest(chatId);
        }
    }

    private void handleRetryCallback(long chatId) throws TelegramApiException {
        if (membershipChecker.checkUserMembership(chatId)) {
            messageSender.sendChannelLinks(chatId);
        } else {
            messageSender.sendMembershipRequest(chatId);
        }
    }
}
