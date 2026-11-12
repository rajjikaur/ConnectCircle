package com.connectcircle.backend.repository;

import com.connectcircle.backend.entity.TrustStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TrustStudyRepository extends JpaRepository<TrustStudy, Long> {
    List<TrustStudy> findByStatusIgnoreCaseOrderByStudyIdAsc(String status);
    Optional<TrustStudy> findByStudyIdAndStatusIgnoreCase(Long studyId, String status);
}
