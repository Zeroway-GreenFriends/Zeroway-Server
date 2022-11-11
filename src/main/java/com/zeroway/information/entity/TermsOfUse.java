package com.zeroway.information.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
// 서비스 이용약관
public class TermsOfUse {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tou_id")
    private Integer id;

    private String content;
    private String url;

    // 공고 날짜
    private LocalDate publishedAt;

    // 시행 날짜
    private LocalDate effectiveAt;
}
