package com.bank.recommendation.controller;


import com.bank.recommendation.models.ResponsRecommendationDto;
import com.bank.recommendation.services.RecommendationsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationsService recommendationsService;

    public RecommendationController(RecommendationsService recommendationsService) {
        this.recommendationsService = recommendationsService;
    }

    @GetMapping("/{userId}")
    public ResponsRecommendationDto getRecommendationToUser(@PathVariable UUID userId) {
        ResponsRecommendationDto result = new ResponsRecommendationDto(userId, recommendationsService.getRecommendationToUser(userId));
        return result;
    }

}
