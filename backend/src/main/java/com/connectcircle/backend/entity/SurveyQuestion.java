package com.connectcircle.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "survey_questions")
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "study_id", nullable = false)
    private TrustStudy study;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "question_type", nullable = false, length = 40)
    private String questionType;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "is_required", nullable = false)
    private Boolean required;

    public Long getQuestionId() { return questionId; }
    public TrustStudy getStudy() { return study; }
    public String getQuestionText() { return questionText; }
    public String getQuestionType() { return questionType; }
    public Integer getDisplayOrder() { return displayOrder; }
    public Boolean getRequired() { return required; }
}
