package com.emi.codegap.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import com.emi.codegap.dto.PlatformAccountDto;
import com.emi.codegap.model.PlatformType;
import com.emi.codegap.service.PlatformLinkService;
import com.emi.codegap.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/platform-accounts")
@RequiredArgsConstructor
public class PlatformLinkController {

    private final PlatformLinkService platformLinkService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<PlatformAccountDto> linkPlatform(
        @Valid @RequestBody LinkPlatformRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        if (!ownsUser(request.userId(), jwt)) {
            return ResponseEntity.notFound().build();
        }
        PlatformAccountDto account = platformLinkService.linkPlatform(request.userId(), request.platform(), request.handle());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlatformAccountDto>> getAccounts(
        @PathVariable UUID userId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        if (!ownsUser(userId, jwt)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(platformLinkService.getAccounts(userId));
    }

    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<Void> unlinkPlatform(
        @PathVariable UUID id,
        @PathVariable UUID userId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        if (!ownsUser(userId, jwt)) {
            return ResponseEntity.notFound().build();
        }
        platformLinkService.unlinkPlatform(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sync")
    public ResponseEntity<Void> syncPlatform(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        platformLinkService.requestSync(id, jwt.getSubject());
        return ResponseEntity.accepted().build();
    }

    private boolean ownsUser(UUID userId, Jwt jwt) {
        return userService.findByKeycloakId(jwt.getSubject())
            .map(profile -> profile.id().equals(userId))
            .orElse(false);
    }

    public record LinkPlatformRequest(
        @NotNull UUID userId,
        @NotNull PlatformType platform,
        @NotBlank String handle
    ) {
    }
}
