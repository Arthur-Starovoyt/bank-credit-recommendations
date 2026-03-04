package com.bank.recommendation.service;

import com.bank.recommendation.entity.DynamicRuleEntity;
import com.bank.recommendation.entity.RuleStatEntity;
import com.bank.recommendation.models.DynamicRuleRequestDto;
import com.bank.recommendation.models.DynamicRuleResponseDto;
import com.bank.recommendation.repositories.DynamicRuleRepository;
import com.bank.recommendation.repositories.RuleStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicRuleService {
    private static final Logger log = LoggerFactory.getLogger(DynamicRuleService.class);

    private final DynamicRuleRepository repository;
    private final RuleStatRepository ruleStatRepository;

    public DynamicRuleService(DynamicRuleRepository repository, RuleStatRepository ruleStatRepository) {
        this.repository = repository;
        this.ruleStatRepository = ruleStatRepository;
    }

    public DynamicRuleResponseDto createRule(DynamicRuleRequestDto request) {
        if (repository.existsByProductId(request.getProductId())) {
            throw new IllegalArgumentException("Rule with product_id " + request.getProductId() + " already exists");
        }
        DynamicRuleEntity rule = new DynamicRuleEntity();
        rule.setProductName(request.getProductName());
        rule.setProductId(request.getProductId());
        rule.setProductText(request.getProductText());
        rule.setRule(request.getRule());
        DynamicRuleEntity savedRule = repository.save(rule);

        // Создаём запись статистики
        RuleStatEntity stat = new RuleStatEntity(savedRule);
        ruleStatRepository.save(stat);

        return new DynamicRuleResponseDto(savedRule);
    }

    public List<DynamicRuleResponseDto> getAllRules() {
        return repository.findAll().stream()
                .map(DynamicRuleResponseDto::new)
                .collect(Collectors.toList());
    }

    public void deleteRuleByProductId(UUID productId) {
        Optional<DynamicRuleEntity> ruleOpt = repository.findByProductId(productId);
        if (ruleOpt.isEmpty()) {
            throw new IllegalArgumentException("Rule with product_id " + productId + " not found");
        }
        DynamicRuleEntity rule = ruleOpt.get();
        // Удаляем статистику, если есть
        ruleStatRepository.findByRuleId(rule.getId()).ifPresent(ruleStatRepository::delete);
        repository.delete(rule);
    }

    // Новый метод для получения всех правил в виде сущностей (нужен для RecommendationsService)
    public List<DynamicRuleEntity> findAllEntities() {
        return repository.findAll();
    }
}