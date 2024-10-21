package com.psouda.filmkoobot.membership;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramMembershipChecker implements MembershipChecker {

    private final TelegramLongPollingBot bot;

    @Value("${telegram.bot.channels}")
    private List<String> channels;

    public TelegramMembershipChecker(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean checkUserMembership(long chatId) throws TelegramApiException {
        for (String channel : channels) {
            GetChatMember getChatMember = new GetChatMember();
            getChatMember.setChatId(channel.trim());
            getChatMember.setUserId(chatId);

            ChatMember member = bot.execute(getChatMember);
            if (!"member".equals(member.getStatus()) && !"administrator".equals(member.getStatus()) && !"creator".equals(member.getStatus())) {
                return false;
            }
        }
        return true;
    }
}
