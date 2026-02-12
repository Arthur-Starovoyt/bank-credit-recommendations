package com.bank.recommendation.interfaces.impl;


import com.bank.recommendation.entity.ProductType;
import com.bank.recommendation.entity.TransactionType;
import com.bank.recommendation.interfaces.RecommendationRuleSet;
import com.bank.recommendation.models.RecomendationDto;
import com.bank.recommendation.repositories.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ImplCreditRecommendation implements RecommendationRuleSet {
    @Autowired
    private RecommendationsRepository repository;

    @Override
    public Optional<RecomendationDto> getRecommendation(UUID user) {
        boolean hasCredit = repository.hasProduct(user, ProductType.CREDIT.name());

        int sumDebitDeposit = repository.getSumByProdTypeAndTransactionsType(user, ProductType.DEBIT.name(), TransactionType.DEPOSIT.name());

        int sumDebitWithdraw = repository.getSumByProdTypeAndTransactionsType(user, ProductType.DEBIT.name(), TransactionType.WITHDRAW.name());

        if (!hasCredit && sumDebitDeposit > sumDebitWithdraw && sumDebitWithdraw > 100000) {
            RecomendationDto investRecommendation = new RecomendationDto("Простой кредит", UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"), """
                    Откройте мир выгодных кредитов с нами!               
                    Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.                    
                    Почему выбирают нас:                    
                    Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.                    
                    Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.                    
                    Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.                    
                    Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!
                    """);
            return Optional.of(investRecommendation);
        }
        return Optional.empty();
    }
}
