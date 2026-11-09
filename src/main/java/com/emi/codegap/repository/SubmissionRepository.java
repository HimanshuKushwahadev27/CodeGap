package com.emi.codegap.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.codegap.entity.SubmissionEntity;
import com.emi.codegap.entity.PlatformAccountEntity;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<SubmissionEntity, UUID> {
    Optional<SubmissionEntity> findByPlatformAccountAndProblemId(
        PlatformAccountEntity platformAccount,
        String problemId
    );
}
