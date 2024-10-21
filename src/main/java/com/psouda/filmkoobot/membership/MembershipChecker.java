package com.psouda.filmkoobot.membership;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface MembershipChecker {
    boolean checkUserMembership(long chatId) throws TelegramApiException;
}

