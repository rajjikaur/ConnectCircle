package com.connectcircle.backend.dto;

public record SurveyAnswerRequest(
        Long questionId,
        Integer ratingValue,
        String answerText) { }
