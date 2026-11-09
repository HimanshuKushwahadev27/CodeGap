package com.emi.codegap.adapter;

import java.time.Instant;
import java.util.Set;

public record PlatformSubmission(
    String problemId,
    String problemName,
    String difficulty,
    Set<String> topicTags,
    Instant solvedAt
) {
}
