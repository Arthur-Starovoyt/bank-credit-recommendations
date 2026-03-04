package com.bank.recommendation.controller;

import com.bank.recommendation.entity.RuleStatEntity;
import com.bank.recommendation.models.DynamicRuleRequestDto;
import com.bank.recommendation.models.DynamicRuleResponseDto;
import com.bank.recommendation.repositories.RuleStatRepository;
import com.bank.recommendation.service.DynamicRuleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("rule")
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;
    private final RuleStatRepository ruleStatRepository;

    public DynamicRuleController(DynamicRuleService dynamicRuleService,
                                 RuleStatRepository ruleStatRepository) {
        this.dynamicRuleService = dynamicRuleService;
        this.ruleStatRepository = ruleStatRepository;
    }

    @PostMapping
    public ResponseEntity<DynamicRuleResponseDto> createRule(@RequestBody @Valid DynamicRuleRequestDto request) {
        try {
            return ResponseEntity.ok(dynamicRuleService.createRule(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DynamicRuleResponseDto>> getAllRules() {
        return ResponseEntity.ok(dynamicRuleService.getAllRules());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        try {
            dynamicRuleService.deleteRuleByProductId(productId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<RuleStatEntity> stats = ruleStatRepository.findAll();
        List<Map<String, Object>> result = stats.stream().map(stat -> {
            Map<String, Object> item = new HashMap<>();
            item.put("rule_id", stat.getRule().getId());
            item.put("count", stat.getCount());
            return item;
        }).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("stats", result);
        return ResponseEntity.ok(response);
    }
}