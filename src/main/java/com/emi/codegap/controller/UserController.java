package com.emi.codegap.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import com.emi.codegap.dto.UserProfileDto;
import com.emi.codegap.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/profile")
    public ResponseEntity<UserProfileDto> upsertProfile(
        @Valid @RequestBody UserProfileRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UserProfileDto profile = userService.createOrGetUser(
            jwt.getSubject(),
            request.displayName(),
            request.email()
        );
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUser(@PathVariable UUID userId, @AuthenticationPrincipal Jwt jwt) {
        return userService.findByKeycloakId(jwt.getSubject())
            .filter(profile -> profile.id().equals(userId))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    public record UserProfileRequest(
        @NotBlank String displayName,
        @NotBlank String email
    ) {
    }
}
