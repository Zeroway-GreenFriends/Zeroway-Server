package com.zeroway.store.entity;

import com.zeroway.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Store extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "store_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String addressNew;

    @Column(length = 100, nullable = false)
    private String addressOld;

    // 경도
    @Column(name = "coord_x")
    private Double coordX;

    // 위도
    @Column(name = "coord_y")
    private Double coordY;

    // 연락처
    @Column(length = 13)
    private String contact;

    @Column(length = 100)
    private String siteUrl;

    // 인스타그램 url
    @Column(length = 100)
    private String instagram;

    private String imageUrl;

    // 운영시간
    private String operatingTime;

    // 취급품목
    private String item;

    // 제로웨이스트 실천 내용
    private String practice;

    // 평균 별점
    private Double scoreAvg;

    public void setScoreAvg(Double scoreAvg) {
        this.scoreAvg = scoreAvg;
    }
}
