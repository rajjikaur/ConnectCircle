package com.connectcircle.backend.dto;

public record SurveyQuestionDto(
        Long questionId,
        String questionText,
        String questionType,
        Integer displayOrder,
        Boolean required) { }
