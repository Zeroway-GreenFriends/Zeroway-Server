package com.zeroway.information.controller;

import com.zeroway.common.BaseException;
import com.zeroway.information.dto.InfoRes;
import com.zeroway.information.service.PrivacyPolicyService;
import com.zeroway.information.service.TermsOfUseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InfoController {

    private final PrivacyPolicyService privacyPolicyService;
    private final TermsOfUseService termsOfUseService;

    /**
     * 최신 버전의 개인정보 처리방침 조회 API
     */
    @GetMapping("/privacy-policy/latest")
    public ResponseEntity<InfoRes> getLatestPrivacyPolicy() {
        return ResponseEntity.ok().body(privacyPolicyService.getLatestPrivacyPolicy());
    }

    /**
     * 특정 버전의 개인정보 처리방침 조회 API
     * @param ppId 개인정보 처리방침 id
     */
    @GetMapping("/privacy-policy/{ppId}")
    public ResponseEntity<InfoRes> getPrivacyPolicy(@PathVariable Integer ppId) throws Exception {
        return ResponseEntity.ok().body(privacyPolicyService.getPrivacyPolicy(ppId));
    }


    /**
     * 최신 버전의 서비스 이용약관 조회 API
     */
    @GetMapping("/terms-of-use/latest")
    public ResponseEntity<InfoRes> getLatestTermsOfUse() {
        return ResponseEntity.ok().body(termsOfUseService.getLatestTermsOfUse());
    }

    /**
     * 특정 버전의 서비스 이용약관 조회 API
     * @param touId 서비스 이용약관 id
     */
    @GetMapping("/terms-of-use/{touId}")
    public ResponseEntity<InfoRes> getTermOfUse(@PathVariable Integer touId) throws Exception {
        return ResponseEntity.ok().body(termsOfUseService.getTermsOfUse(touId));
    }
}
