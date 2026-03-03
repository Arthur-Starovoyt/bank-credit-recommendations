package com.bank.recommendation.controller;

import com.bank.recommendation.service.CacheService;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final BuildProperties buildProperties;
    private final CacheService cacheService;

    public ManagementController(BuildProperties buildProperties, CacheService cacheService) {
        this.buildProperties = buildProperties;
        this.cacheService = cacheService;
    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", buildProperties.getName());
        info.put("version", buildProperties.getVersion());
        return info;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        cacheService.clearAllCaches();
        return ResponseEntity.ok().build();
    }
}