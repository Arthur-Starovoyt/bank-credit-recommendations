package com.bank.recommendation.interfaces;

import com.bank.recommendation.models.RecomendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    public Optional<RecomendationDto> getRecommendation(UUID user);
}
