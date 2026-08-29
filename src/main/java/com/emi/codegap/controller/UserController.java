package com.emi.codegap.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserProfileDto> upsertProfile(@Valid @RequestBody UserProfileRequest request) {
        UserProfileDto profile = userService.createOrGetUser(
            request.keycloakId(),
            request.displayName(),
            request.email()
        );
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUser(@PathVariable UUID userId) {
        return userService.findById(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    public record UserProfileRequest(
        @NotBlank String keycloakId,
        @NotBlank String displayName,
        @NotBlank String email
    ) {
    }
}
