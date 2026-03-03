package com.bank.recommendation.service;

import com.bank.recommendation.entity.DynamicRuleEntity;
import com.bank.recommendation.entity.RuleCondition;
import com.bank.recommendation.interfaces.RecommendationRuleSet;
import com.bank.recommendation.models.RecommendationDto;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationsService {

    private final List<RecommendationRuleSet> staticRules;
    private final DynamicRuleService dynamicRuleService;
    private final com.bank.recommendation.service.DynamicRuleEvaluator ruleEvaluator;

    // Внедрение через конструктор (список статических правил подхватится автоматически)
    public RecommendationsService(List<RecommendationRuleSet> staticRules,
                                  DynamicRuleService dynamicRuleService,
                                  com.bank.recommendation.service.DynamicRuleEvaluator ruleEvaluator) {
        this.staticRules = staticRules;
        this.dynamicRuleService = dynamicRuleService;
        this.ruleEvaluator = ruleEvaluator;
    }

    public List<RecommendationDto> getRecommendationToUser(UUID userId) {
        List<RecommendationDto> recommendations = new ArrayList<>();

        // 1. Статические правила (уже были)
        for (RecommendationRuleSet rule : staticRules) {
            rule.getRecommendation(userId).ifPresent(recommendations::add);
        }

        // 2. Динамические правила
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
            }
        }

        return recommendations;
    }
}