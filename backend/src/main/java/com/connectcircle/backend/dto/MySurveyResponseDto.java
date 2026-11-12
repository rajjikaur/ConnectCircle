package com.connectcircle.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MySurveyResponseDto(
        Long responseId,
        Long studyId,
        String title,
        String topic,
        LocalDateTime submittedAt,
        List<SurveyAnswerResultDto> answers) { }
