package com.emi.codegap.dto;

import java.time.Instant;
import java.util.UUID;

import com.emi.codegap.model.PlatformType;
import com.emi.codegap.model.SyncStatus;

public record PlatformAccountDto(
    UUID id,
    PlatformType platform,
    String handle,
    boolean verified,
    Instant lastSyncedAt,
    SyncStatus syncStatus
) {
}
