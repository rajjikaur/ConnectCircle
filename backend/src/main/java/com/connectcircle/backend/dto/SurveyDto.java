package com.connectcircle.backend.dto;

import java.util.List;

public record SurveyDto(
        Long studyId,
        String title,
        String description,
        String topic,
        List<SurveyQuestionDto> questions) { }
