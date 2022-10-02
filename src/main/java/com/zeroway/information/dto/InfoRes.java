package com.zeroway.information.dto;

import com.zeroway.information.entity.PrivacyPolicy;
import com.zeroway.information.entity.TermsOfUse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InfoRes {
    private Integer id;
    private String content;
    private LocalDate publishedAt;
    private LocalDate effectiveAt;

    public InfoRes(PrivacyPolicy privacyPolicy) {
        this.id = privacyPolicy.getId();
        this.content = privacyPolicy.getContent();
        this.publishedAt = privacyPolicy.getPublishedAt();
        this.effectiveAt = privacyPolicy.getEffectiveAt();
    }

    public InfoRes(TermsOfUse termsOfUse) {
        this.id = termsOfUse.getId();
        this.content = termsOfUse.getContent();
        this.publishedAt = termsOfUse.getPublishedAt();
        this.effectiveAt = termsOfUse.getEffectiveAt();
    }
}
