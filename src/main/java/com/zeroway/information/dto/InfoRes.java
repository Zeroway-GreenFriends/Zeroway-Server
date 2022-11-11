package com.zeroway.information.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zeroway.information.entity.PrivacyPolicy;
import com.zeroway.information.entity.TermsOfUse;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoRes {
    private Integer id;

    @Nullable
    private String content;

    @Nullable
    private String url;
    private LocalDate publishedAt;
    private LocalDate effectiveAt;

    public InfoRes(PrivacyPolicy privacyPolicy) {
        this.id = privacyPolicy.getId();
        this.content = privacyPolicy.getContent();
        this.url = privacyPolicy.getUrl();
        this.publishedAt = privacyPolicy.getPublishedAt();
        this.effectiveAt = privacyPolicy.getEffectiveAt();
    }

    public InfoRes(TermsOfUse termsOfUse) {
        this.id = termsOfUse.getId();
        this.content = termsOfUse.getContent();
        this.url = termsOfUse.getUrl();
        this.publishedAt = termsOfUse.getPublishedAt();
        this.effectiveAt = termsOfUse.getEffectiveAt();
    }
}
