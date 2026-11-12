package com.connectcircle.backend.repository;

import com.connectcircle.backend.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    boolean existsByStudy_StudyIdAndConsumer_Id(Long studyId, Long consumerId);
    List<SurveyResponse> findByConsumer_IdOrderBySubmittedAtDesc(Long consumerId);
}
