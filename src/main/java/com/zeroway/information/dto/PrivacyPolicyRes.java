package com.zeroway.information.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zeroway.information.entity.PrivacyPolicy;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PrivacyPolicyRes {
    private Integer id;
    private String content;
    private LocalDate publishedAt;
    private LocalDate effectiveAt;

    public PrivacyPolicyRes(PrivacyPolicy privacyPolicy) {
        this.id = privacyPolicy.getId();
        this.content = privacyPolicy.getContent();
        this.publishedAt = privacyPolicy.getPublishedAt();
        this.effectiveAt = privacyPolicy.getEffectiveAt();
    }
}
