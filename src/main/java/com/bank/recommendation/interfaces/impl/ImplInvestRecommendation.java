package com.bank.recommendation.interfaces.impl;


import com.bank.recommendation.entity.ProductType;
import com.bank.recommendation.entity.TransactionType;
import com.bank.recommendation.interfaces.RecommendationRuleSet;
import com.bank.recommendation.models.RecommendationDto;
import com.bank.recommendation.repositories.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ImplInvestRecommendation implements RecommendationRuleSet {
    @Autowired
    private RecommendationsRepository repository;

    @Override
    public Optional<RecommendationDto> getRecommendation(UUID user) {
        boolean hasDebit = repository.hasProduct(user, ProductType.DEBIT.name());

        boolean hasInvest = repository.hasProduct(user, ProductType.INVEST.name());

        int sumSavingDeposit = repository.getSumByProdTypeAndTransactionsType(user, ProductType.SAVING.name(), TransactionType.DEPOSIT.name());

        if (hasDebit && !hasInvest && sumSavingDeposit > 1000) {
            RecommendationDto investRecommendation = new RecommendationDto("Invest 500", UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"), "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!");
            return Optional.of(investRecommendation);
        }
        return Optional.empty();
    }
}
