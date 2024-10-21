package com.psouda.filmkoobot.message;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface MessageSender {
    void sendMessage(long chatId, String text) throws TelegramApiException;
    void sendMembershipRequest(long chatId) throws TelegramApiException;
    void sendChannelLinks(long chatId) throws TelegramApiException;
}