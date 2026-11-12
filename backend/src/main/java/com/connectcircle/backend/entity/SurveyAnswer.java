package com.connectcircle.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "survey_answers")
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "response_id", nullable = false)
    private SurveyResponse response;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;

    @Column(name = "rating_value")
    private Integer ratingValue;

    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String answerText;

    public Long getAnswerId() { return answerId; }
    public SurveyResponse getResponse() { return response; }
    public SurveyQuestion getQuestion() { return question; }
    public Integer getRatingValue() { return ratingValue; }
    public String getAnswerText() { return answerText; }
    public void setResponse(SurveyResponse response) { this.response = response; }
    public void setQuestion(SurveyQuestion question) { this.question = question; }
    public void setRatingValue(Integer ratingValue) { this.ratingValue = ratingValue; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}
