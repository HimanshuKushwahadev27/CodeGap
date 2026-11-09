package com.emi.codegap.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.core.KafkaTemplate;

import com.emi.codegap.dto.PlatformAccountDto;
import com.emi.codegap.entity.PlatformAccountEntity;
import com.emi.codegap.entity.UserEntity;
import com.emi.codegap.model.PlatformType;
import com.emi.codegap.model.SyncStatus;
import com.emi.codegap.repository.PlatformAccountRepository;
import com.emi.codegap.repository.UserRepository;
import com.emi.codegap.sync.SyncRequestedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlatformLinkService {

    private final UserRepository userRepository;
    private final PlatformAccountRepository platformAccountRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public PlatformAccountDto linkPlatform(UUID userId, PlatformType platform, String handle) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Optional<PlatformAccountEntity> existing = platformAccountRepository.findByUserAndPlatform(user, platform);
        if (existing.isPresent()) {
            PlatformAccountEntity account = existing.get();
            account.setHandle(handle);
            account.setVerified(false);
            account.setSyncStatus(SyncStatus.PENDING);
            account.setLastSyncedAt(null);
            PlatformAccountDto result = toDto(platformAccountRepository.save(account));
            publishSync(account);
            return result;
        }

        PlatformAccountEntity account = PlatformAccountEntity.builder()
            .user(user)
            .platform(platform)
            .handle(handle)
            .verified(false)
            .syncStatus(SyncStatus.PENDING)
            .build();

        PlatformAccountDto result = toDto(platformAccountRepository.save(account));
        publishSync(account);
        return result;
    }

    @Transactional
    public void unlinkPlatform(UUID userId, UUID accountId) {
        PlatformAccountEntity account = platformAccountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Platform account not found: " + accountId));
        if (!account.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Account does not belong to user");
        }

        platformAccountRepository.delete(account);
    }

    @Transactional
    public void requestSync(UUID accountId) {
        PlatformAccountEntity account = platformAccountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Platform account not found: " + accountId));
        account.setSyncStatus(SyncStatus.PENDING);
        publishSync(account);
    }

    public List<PlatformAccountDto> getAccounts(UUID userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return platformAccountRepository.findByUser(user).stream()
            .map(this::toDto)
            .toList();
    }

    private PlatformAccountDto toDto(PlatformAccountEntity entity) {
        return new PlatformAccountDto(
            entity.getId(),
            entity.getPlatform(),
            entity.getHandle(),
            entity.isVerified(),
            entity.getLastSyncedAt(),
            entity.getSyncStatus()
        );
    }

    private void publishSync(PlatformAccountEntity account) {
        kafkaTemplate.send(
            "sync-requests",
            account.getId().toString(),
            new SyncRequestedEvent(account.getId(), account.getPlatform(), account.getHandle())
        );
    }
}
