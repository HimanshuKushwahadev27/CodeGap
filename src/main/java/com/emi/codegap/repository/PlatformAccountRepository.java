package com.emi.codegap.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.codegap.entity.PlatformAccountEntity;
import com.emi.codegap.entity.UserEntity;
import com.emi.codegap.model.PlatformType;

public interface PlatformAccountRepository extends JpaRepository<PlatformAccountEntity, UUID> {
    List<PlatformAccountEntity> findByUser(UserEntity user);
    Optional<PlatformAccountEntity> findByUserAndPlatform(UserEntity user, PlatformType platform);
    List<PlatformAccountEntity> findBySyncStatus(com.emi.codegap.model.SyncStatus syncStatus);
}
