package com.zeroway.information.service;

import com.zeroway.common.BaseException;
import com.zeroway.information.dto.PrivacyPolicyRes;
import com.zeroway.information.entity.PrivacyPolicy;
import com.zeroway.information.repository.PrivacyPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zeroway.common.BaseResponseStatus.REQUEST_ERROR;

@Service
@RequiredArgsConstructor
public class PrivacyPolicyService {

    private final PrivacyPolicyRepository repository;

    // 가장 최신 개인정보 처리방침 조회
    public PrivacyPolicyRes getLatestPrivacyPolicy() {
        return new PrivacyPolicyRes(repository.findFirstByOrderByPublishedAtDesc());
    }

    // 특정 버전의 개인정보 처리방침 조회
    public PrivacyPolicyRes getPrivacyPolicy(Integer id) throws BaseException {
        return new PrivacyPolicyRes(repository.findById(id)
                .orElseThrow(() -> new BaseException(REQUEST_ERROR)));
    }

}
