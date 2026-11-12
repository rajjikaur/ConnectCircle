package com.connectcircle.backend.controller;

import com.connectcircle.backend.dto.SubmitSurveyRequest;
import com.connectcircle.backend.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/consumer/surveys")
public class SurveyController {
    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping
    public ResponseEntity<?> getActiveSurveys() {
        return ResponseEntity.ok(surveyService.getActiveSurveys());
    }

    @PostMapping("/{studyId}/submit")
    public ResponseEntity<?> submitSurvey(@PathVariable Long studyId,
                                          @RequestBody SubmitSurveyRequest request,
                                          Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(surveyService.submitSurvey(studyId, authentication.getName(), request));
    }

    @GetMapping("/my-responses")
    public ResponseEntity<?> getMyResponses(Authentication authentication) {
        return ResponseEntity.ok(surveyService.getMyResponses(authentication.getName()));
    }
}
