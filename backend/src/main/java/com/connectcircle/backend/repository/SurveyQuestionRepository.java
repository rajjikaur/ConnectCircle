package com.connectcircle.backend.repository;

import com.connectcircle.backend.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
    List<SurveyQuestion> findByStudy_StudyIdOrderByDisplayOrderAsc(Long studyId);
}
