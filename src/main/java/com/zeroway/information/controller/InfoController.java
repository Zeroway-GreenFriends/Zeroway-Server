package com.zeroway.information.controller;

import com.zeroway.common.BaseException;
import com.zeroway.information.service.PrivacyPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InfoController {

    private final PrivacyPolicyService privacyPolicyService;

    /**
     * 최신 개인정보 처리방침 조회 API
     */
    @GetMapping("/privacy-policy/latest")
    public ResponseEntity<?> getLatestPrivacyPolicy() {
        return ResponseEntity.ok().body(privacyPolicyService.getLatestPrivacyPolicy());
    }

    /**
     * id로 개인정보 처리방침 조회 API
     * @param ppId 개인정보 처리방침 id
     */
    @GetMapping("/privacy-policy/{ppId}")
    public ResponseEntity<?> getPrivacyPolicy(@PathVariable Integer ppId) throws Exception {
        return ResponseEntity.ok().body(privacyPolicyService.getPrivacyPolicy(ppId));
    }
}
