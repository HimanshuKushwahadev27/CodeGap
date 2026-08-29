package com.emi.codegap.dto;

import java.util.UUID;

public record UserProfileDto(
    UUID id,
    String keycloakId,
    String displayName,
    String email
) {
}
