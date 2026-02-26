package com.bank.recommendation.controller;


import com.bank.recommendation.models.ResponseRecommendationDto;
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
    public ResponseRecommendationDto getRecommendationToUser(@PathVariable UUID userId) {
        ResponseRecommendationDto result = new ResponseRecommendationDto(userId, recommendationsService.getRecommendationToUser(userId));
        return result;
    }

}
