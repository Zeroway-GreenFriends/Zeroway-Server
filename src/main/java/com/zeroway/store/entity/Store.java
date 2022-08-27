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

    @Column(length = 100)
    private String addressNew;

    @Column(length = 100)
    private String addressOld;

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

    // 소개 및 제로웨이스트 실천 내용
    private String description;

    public Store(String name, String addressNew, String addressOld, String contact, String siteUrl, String instagram, String imageUrl, String operatingTime, String item, String description) {
        this.name = name;
        this.addressNew = addressNew;
        this.addressOld = addressOld;
        this.contact = contact;
        this.siteUrl = siteUrl;
        this.instagram = instagram;
        this.imageUrl = imageUrl;
        this.operatingTime = operatingTime;
        this.item = item;
        this.description = description;
    }

    public Store(String name) {
        this.name = name;
    }
}
