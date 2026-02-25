package com.bank.recommendation.repositories;

import com.bank.recommendation.entity.DynamicRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRuleEntity, UUID> {
    void deleteByProductId(@Param("productId") UUID productId);

    boolean existsByProductId(UUID productId);

    Optional<DynamicRuleEntity> findByProductId(UUID productId);
}
