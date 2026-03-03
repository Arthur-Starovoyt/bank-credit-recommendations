package com.bank.recommendation.controller;

import com.bank.recommendation.models.DynamicRuleRequestDto;
import com.bank.recommendation.models.DynamicRuleResponseDto;
import com.bank.recommendation.service.DynamicRuleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("rule")
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;

    public DynamicRuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
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
}
