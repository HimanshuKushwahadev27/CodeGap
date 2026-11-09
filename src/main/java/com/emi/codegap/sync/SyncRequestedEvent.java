package com.emi.codegap.sync;

import java.util.UUID;

import com.emi.codegap.model.PlatformType;

public record SyncRequestedEvent(UUID accountId, PlatformType platform, String handle) {
}
