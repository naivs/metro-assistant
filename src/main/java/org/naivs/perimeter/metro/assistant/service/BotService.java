package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.config.TBotConfig;
import org.naivs.perimeter.metro.assistant.telegram.MyBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TBotConfig tBotConfig;
    private MyBot bot;

    @PostConstruct
    private void init() {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        bot = new MyBot(tBotConfig.getName(), tBotConfig.getKey());

        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        tBotConfig.getRecipientList()
                .forEach(recipient -> bot.sendMessage(message, recipient));
    }
}
