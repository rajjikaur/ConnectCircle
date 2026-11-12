package com.connectcircle.backend.dto;

public record SurveyAnswerResultDto(
        Long questionId,
        String questionText,
        Integer ratingValue,
        String answerText) { }
