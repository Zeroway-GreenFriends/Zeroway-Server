package com.zeroway.information.repository;

import com.zeroway.information.entity.TermsOfUse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsOfUseRepository extends JpaRepository<TermsOfUse, Integer> {

    // 가장 최신 조회
    TermsOfUse findFirstByOrderByPublishedAtDesc();
}
