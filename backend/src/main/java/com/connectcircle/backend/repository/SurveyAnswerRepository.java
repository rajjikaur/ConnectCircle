package com.connectcircle.backend.repository;

import com.connectcircle.backend.entity.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {
    List<SurveyAnswer> findByResponse_ResponseIdOrderByAnswerIdAsc(Long responseId);
}
