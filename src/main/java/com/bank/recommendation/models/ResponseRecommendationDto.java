package com.bank.recommendation.models;

import java.util.List;
import java.util.UUID;

public class ResponseRecommendationDto {
    private UUID user_id;
    private List<RecommendationDto> recommendation;

    public ResponseRecommendationDto(UUID user_id, List<RecommendationDto> recommendation) {
        this.user_id = user_id;
        this.recommendation = recommendation;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public List<RecommendationDto> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(List<RecommendationDto> recommendation) {
        this.recommendation = recommendation;
    }
}
