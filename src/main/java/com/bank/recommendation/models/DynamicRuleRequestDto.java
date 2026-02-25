package com.bank.recommendation.models;

import com.bank.recommendation.entity.RuleCondition;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class DynamicRuleRequestDto {
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("product_text")
    private String productText;

    @JsonProperty("rule")
    private List<RuleCondition> rule;

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
