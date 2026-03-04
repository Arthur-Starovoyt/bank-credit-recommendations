package com.bank.recommendation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "rule_stat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleStatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "rule_id", referencedColumnName = "id", nullable = false, unique = true)
    private DynamicRuleEntity rule;

    @Column(name = "count", nullable = false)
    private int count = 0;

    //  конструктор для создания с rule и count по умолчанию 0
    public RuleStatEntity(DynamicRuleEntity rule) {
        this.rule = rule;
        this.count = 0;
    }

    public void incrementCount() {
        this.count++;
    }
}