package org.naivs.perimeter.metro.assistant.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    private final String name;
    private final String key;

    private static final String responseStub =
            "Приветствую вас! К сожалению, меня не обучили вести диалог. " +
                    "Я умею только информировать о чем меня попросили.";

    public MyBot(String name, String key) {
        super();
        this.name = name;
        this.key = key;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(responseStub);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return key;
    }

    public void sendMessage(String text, String chatId) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
