package com.connectcircle.backend.dto;

import java.util.List;

public record SubmitSurveyRequest(
        Boolean consentConfirmed,
        List<SurveyAnswerRequest> answers) { }
