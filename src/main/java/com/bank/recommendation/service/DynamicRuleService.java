package com.bank.recommendation.service;

import com.bank.recommendation.entity.DynamicRuleEntity;
import com.bank.recommendation.models.DynamicRuleRequestDto;
import com.bank.recommendation.models.DynamicRuleResponseDto;
import com.bank.recommendation.repositories.DynamicRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicRuleService {
    private static final Logger log = LoggerFactory.getLogger(DynamicRuleService.class);
    private final DynamicRuleRepository repository;

    public DynamicRuleService(DynamicRuleRepository repository) {
        this.repository = repository;
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
        return new DynamicRuleResponseDto(savedRule);
    }

    public List<DynamicRuleResponseDto> getAllRules() {
        return repository.findAll().stream()
                .map(DynamicRuleResponseDto::new)
                .collect(Collectors.toList());
    }

    public void deleteRuleByProductId(UUID productId) {
        if (!repository.existsByProductId(productId)) {
            throw new IllegalArgumentException("Rule with product_id " + productId + " not found");
        }
        repository.deleteByProductId(productId);
    }
    public List<DynamicRuleEntity> findAllEntities() {
        return repository.findAll();
    }
}
