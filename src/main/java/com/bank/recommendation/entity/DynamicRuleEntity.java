package com.bank.recommendation.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table (name = "dynamic_rule")
public class DynamicRuleEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_name",nullable = false)
    private String productName;

    @Column(name = "product_id",nullable = false,unique = true)
    private UUID productId;

    @Column(name = "product_text",nullable = false,columnDefinition = "TEXT")
    private String productText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="rule_json",nullable = false,columnDefinition = "jsonb")
    private List<RuleCondition> rule;

    public DynamicRuleEntity(){
    }

    public DynamicRuleEntity(UUID id, String productName, UUID productId, String productText, List<RuleCondition> rule) {
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
