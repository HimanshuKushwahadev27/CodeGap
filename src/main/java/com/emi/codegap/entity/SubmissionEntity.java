package com.emi.codegap.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "submissions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"platform_account_id", "problem_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "platform_account_id", nullable = false)
    @jakarta.persistence.ManyToOne(fetch = FetchType.LAZY, optional = false)
    private PlatformAccountEntity platformAccount;

    @Column(name = "problem_id", nullable = false)
    private String problemId;

    @Column(name = "problem_name", nullable = false)
    private String problemName;

    @Column(nullable = false)
    private String platform;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "solved_at", nullable = false)
    private Instant solvedAt;

    @ElementCollection
    @CollectionTable(name = "submission_topic_tags", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "topic")
    private java.util.Set<String> topicTags;
}
