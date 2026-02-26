package com.bank.recommendation.interfaces;

import com.bank.recommendation.models.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    public Optional<RecommendationDto> getRecommendation(UUID user);
}
