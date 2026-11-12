package com.connectcircle.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "survey_responses")
public class SurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Long responseId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_id", nullable = false)
    private TrustStudy study;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consumer_user_id", nullable = false)
    private User consumer;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "consent_confirmed", nullable = false)
    private Boolean consentConfirmed;

    public Long getResponseId() { return responseId; }
    public TrustStudy getStudy() { return study; }
    public User getConsumer() { return consumer; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public Boolean getConsentConfirmed() { return consentConfirmed; }

    public void setStudy(TrustStudy study) { this.study = study; }
    public void setConsumer(User consumer) { this.consumer = consumer; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public void setConsentConfirmed(Boolean consentConfirmed) { this.consentConfirmed = consentConfirmed; }
}
