package com.emi.codegap.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emi.codegap.adapter.CodeforcesApiClient;
import com.emi.codegap.adapter.LeetCodeApiClient;
import com.emi.codegap.adapter.PlatformSubmission;
import com.emi.codegap.entity.PlatformAccountEntity;
import com.emi.codegap.entity.SubmissionEntity;
import com.emi.codegap.model.PlatformType;
import com.emi.codegap.model.SyncStatus;
import com.emi.codegap.repository.PlatformAccountRepository;
import com.emi.codegap.repository.SubmissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlatformSyncService {

    private final PlatformAccountRepository platformAccountRepository;
    private final SubmissionRepository submissionRepository;
    private final CodeforcesApiClient codeforcesApiClient;
    private final LeetCodeApiClient leetCodeApiClient;

    @Transactional
    public int sync(UUID accountId, PlatformType platform, String handle) {
        PlatformAccountEntity account = platformAccountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Platform account not found: " + accountId));
        account.setSyncStatus(SyncStatus.SYNCING);
        List<PlatformSubmission> submissions = adapter(platform).fetchSubmissions(handle);
        submissions.forEach(submission -> save(account, platform, submission));
        account.setSyncStatus(SyncStatus.SYNCED);
        account.setVerified(true);
        account.setLastSyncedAt(Instant.now());
        return submissions.size();
    }

    @Transactional
    public void markFailed(UUID accountId) {
        platformAccountRepository.findById(accountId).ifPresent(account -> {
            account.setSyncStatus(SyncStatus.FAILED);
            platformAccountRepository.save(account);
        });
    }

    private void save(PlatformAccountEntity account, PlatformType platform, PlatformSubmission submission) {
        SubmissionEntity entity = submissionRepository
            .findByPlatformAccountAndProblemId(account, submission.problemId())
            .orElseGet(SubmissionEntity::new);
        entity.setPlatformAccount(account);
        entity.setProblemId(submission.problemId());
        entity.setProblemName(submission.problemName());
        entity.setPlatform(platform.name());
        entity.setDifficulty(submission.difficulty());
        entity.setTopicTags(submission.topicTags());
        entity.setSolvedAt(submission.solvedAt());
        submissionRepository.save(entity);
    }

    private com.emi.codegap.adapter.PlatformAdapter adapter(PlatformType platform) {
        return switch (platform) {
            case CODEFORCES -> codeforcesApiClient;
            case LEETCODE -> leetCodeApiClient;
            case GFG -> throw new UnsupportedOperationException("GFG synchronization is not implemented");
        };
    }
}
