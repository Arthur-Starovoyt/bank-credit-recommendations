package com.bank.recommendation.models;

import java.util.List;
import java.util.UUID;

public class ResponsRecommendationDto {
    private UUID user_id;
    private List<RecomendationDto> recommendation;

    public ResponsRecommendationDto(UUID user_id, List<RecomendationDto> recommendation) {
        this.user_id = user_id;
        this.recommendation = recommendation;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public List<RecomendationDto> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<RecomendationDto> recommendation) {
        this.recommendation = recommendation;
    }
}
