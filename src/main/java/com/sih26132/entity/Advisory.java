package com.sih26132.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "advisories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advisory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseEntity;

    @Column(nullable = false, length = 10)
    private String language;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "immediate_actions", columnDefinition = "TEXT")
    private String immediateActions;

    @Column(name = "preventive_actions", columnDefinition = "TEXT")
    private String preventiveActions;

    @Column(name = "ipdm_actions", columnDefinition = "TEXT")
    private String ipdmActions;

    @Column(name = "safe_use_instructions", columnDefinition = "TEXT")
    private String safeUseInstructions;

    @Column(name = "when_to_contact_expert", columnDefinition = "TEXT")
    private String whenToContactExpert;

    @Column(name = "when_to_recheck", columnDefinition = "TEXT")
    private String whenToRecheck;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "knowledge_sources",
            columnDefinition = "jsonb"
    )
    private Map<String, Object> knowledgeSources;

    @Column(name = "generated_by")
    private String generatedBy;

    @Column(length = 30)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
