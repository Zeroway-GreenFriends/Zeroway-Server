package com.zeroway.information.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
// 개인정보 처리방침
public class PrivacyPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pp_id")
    private Integer id;

    private String content;

    private String Url;

    // 공고 날짜
    private LocalDate publishedAt;

    // 시행 날짜
    private LocalDate effectiveAt;
}
