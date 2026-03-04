package com.bank.recommendation.repositories;

import com.bank.recommendation.entity.RuleStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleStatRepository extends JpaRepository<RuleStatEntity, UUID> {
    Optional<RuleStatEntity> findByRuleId(UUID ruleId);
}