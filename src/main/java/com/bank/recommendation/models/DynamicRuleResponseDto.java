package com.bank.recommendation.models;

import com.bank.recommendation.entity.DynamicRuleEntity;
import com.bank.recommendation.entity.RuleCondition;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class DynamicRuleResponseDto {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_text")
    private String productText;

    @JsonProperty("rule")
    private List<RuleCondition> rule;

    public DynamicRuleResponseDto() {
    }

    public DynamicRuleResponseDto(DynamicRuleEntity entity) {
        this.id = entity.getId();
        this.productName = entity.getProductName();
        this.productId = entity.getProductId();
        this.productText = entity.getProductText();
        this.rule = entity.getRule();
    }

    public DynamicRuleResponseDto(UUID id, String productName, UUID productId, String productText, List<RuleCondition> rule) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rule = rule;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<RuleCondition> getRule() {
        return rule;
    }

    public void setRule(List<RuleCondition> rule) {
        this.rule = rule;
    }
}
