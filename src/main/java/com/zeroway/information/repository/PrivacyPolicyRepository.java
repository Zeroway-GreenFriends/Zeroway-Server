package com.zeroway.information.repository;

import com.zeroway.information.entity.PrivacyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivacyPolicyRepository extends JpaRepository<PrivacyPolicy, Integer> {

    // 가장 최신 조회
    PrivacyPolicy findFirstByOrderByPublishedAtDesc();
}
