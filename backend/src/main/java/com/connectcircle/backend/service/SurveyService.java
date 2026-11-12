package com.connectcircle.backend.service;

import com.connectcircle.backend.dto.*;
import com.connectcircle.backend.entity.*;
import com.connectcircle.backend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    private final TrustStudyRepository studyRepository;
    private final SurveyQuestionRepository questionRepository;
    private final SurveyResponseRepository responseRepository;
    private final SurveyAnswerRepository answerRepository;
    private final UserRepository userRepository;

    public SurveyService(TrustStudyRepository studyRepository,
                         SurveyQuestionRepository questionRepository,
                         SurveyResponseRepository responseRepository,
                         SurveyAnswerRepository answerRepository,
                         UserRepository userRepository) {
        this.studyRepository = studyRepository;
        this.questionRepository = questionRepository;
        this.responseRepository = responseRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<SurveyDto> getActiveSurveys() {
        LocalDateTime now = LocalDateTime.now();
        return studyRepository.findByStatusIgnoreCaseOrderByStudyIdAsc("ACTIVE")
                .stream()
                .filter(study -> study.getStartsAt() == null || !study.getStartsAt().isAfter(now))
                .filter(study -> study.getEndsAt() == null || !study.getEndsAt().isBefore(now))
                .map(study -> new SurveyDto(
                        study.getStudyId(), study.getTitle(), study.getDescription(), study.getTopic(),
                        questionRepository.findByStudy_StudyIdOrderByDisplayOrderAsc(study.getStudyId())
                                .stream().map(this::toQuestionDto).toList()))
                .toList();
    }

    @Transactional
    public MySurveyResponseDto submitSurvey(Long studyId, String email, SubmitSurveyRequest request) {
        User consumer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged-in user not found"));

        boolean isConsumer = consumer.getRoles().stream()
                .anyMatch(role -> "CONSUMER".equalsIgnoreCase(role.getName()));
        if (!isConsumer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only Consumer accounts can submit trust surveys");
        }

        TrustStudy study = studyRepository.findByStudyIdAndStatusIgnoreCase(studyId, "ACTIVE")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active survey not found"));

        LocalDateTime now = LocalDateTime.now();
        if ((study.getStartsAt() != null && study.getStartsAt().isAfter(now)) ||
            (study.getEndsAt() != null && study.getEndsAt().isBefore(now))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This survey is not currently accepting responses");
        }

        if (responseRepository.existsByStudy_StudyIdAndConsumer_Id(studyId, consumer.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already submitted this survey");
        }

        List<SurveyQuestion> questions = questionRepository.findByStudy_StudyIdOrderByDisplayOrderAsc(studyId);
        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This survey has no questions configured");
        }
        if (request == null || request.answers() == null || request.answers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please answer the survey before submitting");
        }

        Map<Long, SurveyAnswerRequest> submitted = new HashMap<>();
        for (SurveyAnswerRequest answer : request.answers()) {
            if (answer == null || answer.questionId() == null || submitted.put(answer.questionId(), answer) != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or duplicate question answer");
            }
        }

        for (SurveyQuestion question : questions) {
            SurveyAnswerRequest answer = submitted.get(question.getQuestionId());
            if (Boolean.TRUE.equals(question.getRequired()) && answer == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please answer all required questions");
            }
            if (answer != null && "RATING_1_5".equalsIgnoreCase(question.getQuestionType())) {
                if (answer.ratingValue() == null || answer.ratingValue() < 1 || answer.ratingValue() > 5) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ratings must be between 1 and 5");
                }
            }
        }

        Set<Long> validQuestionIds = questions.stream().map(SurveyQuestion::getQuestionId).collect(Collectors.toSet());
        if (!validQuestionIds.containsAll(submitted.keySet())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An answer belongs to a different survey");
        }

        SurveyResponse response = new SurveyResponse();
        response.setStudy(study);
        response.setConsumer(consumer);
        response.setSubmittedAt(now);
        response.setConsentConfirmed(Boolean.TRUE.equals(request.consentConfirmed()));
        response = responseRepository.save(response);

        List<SurveyAnswer> answersToSave = new ArrayList<>();
        for (SurveyQuestion question : questions) {
            SurveyAnswerRequest answerRequest = submitted.get(question.getQuestionId());
            if (answerRequest == null) continue;
            SurveyAnswer answer = new SurveyAnswer();
            answer.setResponse(response);
            answer.setQuestion(question);
            answer.setRatingValue(answerRequest.ratingValue());
            answer.setAnswerText(answerRequest.answerText());
            answersToSave.add(answer);
        }
        answerRepository.saveAll(answersToSave);
        return toResponseDto(response, answersToSave);
    }

    @Transactional(readOnly = true)
    public List<MySurveyResponseDto> getMyResponses(String email) {
        User consumer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Logged-in user not found"));
        return responseRepository.findByConsumer_IdOrderBySubmittedAtDesc(consumer.getId())
                .stream()
                .map(response -> toResponseDto(response,
                        answerRepository.findByResponse_ResponseIdOrderByAnswerIdAsc(response.getResponseId())))
                .toList();
    }

    private SurveyQuestionDto toQuestionDto(SurveyQuestion question) {
        return new SurveyQuestionDto(question.getQuestionId(), question.getQuestionText(),
                question.getQuestionType(), question.getDisplayOrder(), question.getRequired());
    }

    private MySurveyResponseDto toResponseDto(SurveyResponse response, List<SurveyAnswer> answers) {
        return new MySurveyResponseDto(
                response.getResponseId(), response.getStudy().getStudyId(), response.getStudy().getTitle(),
                response.getStudy().getTopic(), response.getSubmittedAt(),
                answers.stream().map(answer -> new SurveyAnswerResultDto(
                        answer.getQuestion().getQuestionId(), answer.getQuestion().getQuestionText(),
                        answer.getRatingValue(), answer.getAnswerText())).toList());
    }
}
