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

import com.emi.codegap.dto.PlatformAccountDto;
import com.emi.codegap.model.PlatformType;
import com.emi.codegap.service.PlatformLinkService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/platform-accounts")
@RequiredArgsConstructor
public class PlatformLinkController {

    private final PlatformLinkService platformLinkService;

    @PostMapping
    public ResponseEntity<PlatformAccountDto> linkPlatform(@Valid @RequestBody LinkPlatformRequest request) {
        PlatformAccountDto account = platformLinkService.linkPlatform(request.userId(), request.platform(), request.handle());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlatformAccountDto>> getAccounts(@PathVariable UUID userId) {
        return ResponseEntity.ok(platformLinkService.getAccounts(userId));
    }

    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<Void> unlinkPlatform(@PathVariable UUID id, @PathVariable UUID userId) {
        platformLinkService.unlinkPlatform(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sync")
    public ResponseEntity<Void> syncPlatform(@PathVariable UUID id) {
        platformLinkService.requestSync(id);
        return ResponseEntity.accepted().build();
    }

    public record LinkPlatformRequest(
        @NotNull UUID userId,
        @NotNull PlatformType platform,
        @NotBlank String handle
    ) {
    }
}
