package com.emi.codegap.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emi.codegap.dto.UserProfileDto;
import com.emi.codegap.entity.UserEntity;
import com.emi.codegap.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserProfileDto createOrGetUser(String keycloakId, String displayName, String email) {
        Optional<UserEntity> existing = userRepository.findByKeycloakId(keycloakId);
        if (existing.isPresent()) {
            UserEntity user = existing.get();
            user.setDisplayName(displayName);
            user.setEmail(email);
            return toDto(userRepository.save(user));
        }

        UserEntity user = UserEntity.builder()
            .keycloakId(keycloakId)
            .displayName(displayName)
            .email(email)
            .createdAt(Instant.now())
            .build();

        return toDto(userRepository.save(user));
    }

    public Optional<UserProfileDto> findByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
            .map(this::toDto);
    }

    public Optional<UserProfileDto> findById(UUID userId) {
        return userRepository.findById(userId)
            .map(this::toDto);
    }

    private UserProfileDto toDto(UserEntity user) {
        return new UserProfileDto(
            user.getId(),
            user.getKeycloakId(),
            user.getDisplayName(),
            user.getEmail()
        );
    }
}
