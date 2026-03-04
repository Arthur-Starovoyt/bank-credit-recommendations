package com.bank.recommendation.service;

import com.bank.recommendation.entity.DynamicRuleEntity;
import com.bank.recommendation.entity.RuleCondition;
import com.bank.recommendation.entity.RuleStatEntity;
import com.bank.recommendation.interfaces.RecommendationRuleSet;
import com.bank.recommendation.models.RecommendationDto;
import com.bank.recommendation.service.DynamicRuleEvaluator;
import com.bank.recommendation.repositories.RuleStatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RecommendationsService {

    private final List<RecommendationRuleSet> staticRules;
    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleEvaluator ruleEvaluator;
    private final RuleStatRepository ruleStatRepository;

    public RecommendationsService(List<RecommendationRuleSet> staticRules,
                                  DynamicRuleService dynamicRuleService,
                                  DynamicRuleEvaluator ruleEvaluator,
                                  RuleStatRepository ruleStatRepository) {
        this.staticRules = staticRules;
        this.dynamicRuleService = dynamicRuleService;
        this.ruleEvaluator = ruleEvaluator;
        this.ruleStatRepository = ruleStatRepository;
    }

    public List<RecommendationDto> getRecommendationToUser(UUID userId) {
        List<RecommendationDto> recommendations = new ArrayList<>();

        // Статические правила
        for (RecommendationRuleSet rule : staticRules) {
            rule.getRecommendation(userId).ifPresent(recommendations::add);
        }

        // Динамические правила
        List<DynamicRuleEntity> dynamicRules = dynamicRuleService.findAllEntities();
        for (DynamicRuleEntity rule : dynamicRules) {
            boolean allConditionsMet = true;
            for (RuleCondition condition : rule.getRule()) {
                if (!ruleEvaluator.evaluate(condition, userId)) {
                    allConditionsMet = false;
                    break;
                }
            }
            if (allConditionsMet) {
                recommendations.add(new RecommendationDto(
                        rule.getProductName(),
                        rule.getProductId(),
                        rule.getProductText()
                ));
                // Увеличиваем счётчик статистики
                System.out.println(">>> Увеличиваем счётчик для правила: " + rule.getId());
                ruleStatRepository.findByRuleId(rule.getId()).ifPresent(stat -> {
                    stat.incrementCount();
                    ruleStatRepository.save(stat);
                    System.out.println(">>> Счётчик сохранён, новое значение: " + stat.getCount());
                });
            }
        }

        return recommendations;
    }
}