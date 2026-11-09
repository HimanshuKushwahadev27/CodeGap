package com.emi.codegap.sync;

import java.util.UUID;

import com.emi.codegap.model.PlatformType;

public record SyncCompletedEvent(UUID accountId, PlatformType platform, int submissionCount) {
}
