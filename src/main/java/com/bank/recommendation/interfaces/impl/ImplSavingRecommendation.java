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
public class ImplSavingRecommendation implements RecommendationRuleSet {
    @Autowired
    private RecommendationsRepository repository;

    @Override
    public Optional<RecomendationDto> getRecommendation(UUID user) {
        boolean hasDebit = repository.hasProduct(user, ProductType.DEBIT.name());

        int sumDebitDeposit = repository.getSumByProdTypeAndTransactionsType(user, ProductType.DEBIT.name(), TransactionType.DEPOSIT.name());

        int sumSavingDeposit = repository.getSumByProdTypeAndTransactionsType(user, ProductType.SAVING.name(), TransactionType.DEPOSIT.name());

        int sumDebitWithdraw = repository.getSumByProdTypeAndTransactionsType(user, ProductType.DEBIT.name(), TransactionType.WITHDRAW.name());

        if (hasDebit && (sumDebitDeposit >= 50000 || sumSavingDeposit >= 50000) && sumDebitDeposit > sumDebitWithdraw) {
            RecomendationDto investRecommendation = new RecomendationDto("Top Saving", UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"), """
                    Откройте свою собственную «Копилку» с нашим банком!
                    «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!
                    Преимущества «Копилки»:
                    Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.
                    Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.
                    Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.
                    Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!
                    """);
            return Optional.of(investRecommendation);
        }
        return Optional.empty();
    }
}
