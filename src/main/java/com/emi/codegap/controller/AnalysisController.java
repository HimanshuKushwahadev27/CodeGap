package com.emi.codegap.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import com.emi.codegap.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalysisController {

    private final UserService userService;

    @GetMapping("/analysis/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(
        @RequestParam UUID userId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        if (userService.findByKeycloakId(jwt.getSubject())
            .filter(profile -> profile.id().equals(userId))
            .isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> payload = Map.of(
            "userId", userId,
            "overview", Map.of(
                "totalPlatformsLinked", 0,
                "strengthScore", 0,
                "weakestTopic", "No data yet"
            ),
            "topics", List.of(),
            "recommendations", List.of(),
            "emptyState", true
        );
        return ResponseEntity.ok(payload);
    }
}
