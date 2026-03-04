package com.bank.recommendation.telegram;

import com.bank.recommendation.models.RecommendationDto;
import com.bank.recommendation.repositories.RecommendationsRepository;
import com.bank.recommendation.service.RecommendationsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CreditBot extends TelegramLongPollingBot {

    private final RecommendationsService recommendationsService;
    private final RecommendationsRepository recommendationsRepository;
    private final String botToken;
    private final String botUsername = "bank_credit_recommendations_bot"; // ваше имя бота

    public CreditBot(@Value("${telegram.bot.token}") String botToken,
                     RecommendationsService recommendationsService,
                     RecommendationsRepository recommendationsRepository) {
        this.botToken = botToken;
        this.recommendationsService = recommendationsService;
        this.recommendationsRepository = recommendationsRepository;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/start")) {
                sendMessage(chatId, "Привет! Я бот для выдачи рекомендаций банковских продуктов. Используй /recommend <username> чтобы получить рекомендации.");
            } else if (messageText.startsWith("/recommend")) {
                String[] parts = messageText.split(" ", 2);
                if (parts.length < 2 || parts[1].trim().isEmpty()) {
                    sendMessage(chatId, "Пожалуйста, укажите имя пользователя после команды, например: /recommend Ivan");
                    return;
                }
                String username = parts[1].trim();
                Optional<UUID> userIdOpt = recommendationsRepository.findUserIdByUsername(username);
                if (userIdOpt.isEmpty()) {
                    sendMessage(chatId, "Пользователь с именем '" + username + "' не найден.");
                    return;
                }
                UUID userId = userIdOpt.get();
                List<RecommendationDto> recommendations = recommendationsService.getRecommendationToUser(userId);
                if (recommendations.isEmpty()) {
                    sendMessage(chatId, "Для пользователя " + username + " нет рекомендаций.");
                } else {
                    StringBuilder response = new StringBuilder("Здравствуйте, " + username + "!\nНовые продукты для вас:\n");
                    for (RecommendationDto rec : recommendations) {
                        response.append("- ").append(rec.getName()).append(": ").append(rec.getText()).append("\n");
                    }
                    sendMessage(chatId, response.toString());
                }
            } else {
                sendMessage(chatId, "Неизвестная команда. Используй /start или /recommend <username>");
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}