package com.psouda.filmkoobot.message;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramMessageSender implements MessageSender {

    private final TelegramLongPollingBot bot;

    public TelegramMessageSender(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        bot.execute(message);
    }

    @Override
    public void sendMembershipRequest(long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("لطفاً ابتدا عضو کانال‌های زیر شوید و سپس روی دکمه بررسی مجدد کلیک کنید:");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // دکمه‌های عضویت در کانال‌ها
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(createButtonWithUrl("عضویت در کانال گل یا پوچ", "https://t.me/GoleYaPoche"));
        row1.add(createButtonWithUrl("عضویت در کانال بازنده", "https://t.me/BazandehChannel"));
        row1.add(createButtonWithUrl("عضویت در کانال گردن زنی", "https://t.me/GardanZani"));

        // دکمه بررسی مجدد
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(createButton("بررسی مجدد", "retry"));

        rowsInline.add(row1);
        rowsInline.add(row2);
        inlineKeyboardMarkup.setKeyboard(rowsInline);

        message.setReplyMarkup(inlineKeyboardMarkup);
        bot.execute(message);
    }

    @Override
    public void sendChannelLinks(long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("شما عضو کانال‌های مورد نظر شدید. برای دانلود فایل‌ها، لطفاً وارد کانال‌های زیر شوید:\n\n" +
                "🔗 [کانال گل یا پوچ](https://t.me/GoleYaPoche)\n" +
                "🔗 [کانال بازنده](https://t.me/BazandehChannel)\n" +
                "🔗 [کانال گردن زنی](https://t.me/GardanZani)");
        message.setParseMode("Markdown");
        bot.execute(message);
    }

    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private InlineKeyboardButton createButtonWithUrl(String text, String url) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setUrl(url);
        return button;
    }
}
