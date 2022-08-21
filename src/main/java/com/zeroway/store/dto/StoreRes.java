package com.zeroway.store.dto;

import com.zeroway.store.entity.Store;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 제로웨이스트 샵 상세 조회 응답 데이터
 */
@Data
public class StoreRes {
    private String imageUrl;
    private String name;
    private String item;
    private Double scoreAvg;
    private String addressNew;
    private String operatingTime;
    private String contact;
    private String siteUrl;
    private String instagram;
    private int reviewCount = 0;
    private List<ReviewInfo> reviewList = new ArrayList<>();

    public StoreRes(Store store, List<ReviewInfo> reviewList, int reviewCount) {
        this.imageUrl = store.getImageUrl();
        this.name = store.getName();
        this.item = store.getItem();
        this.scoreAvg = store.getScoreAvg();
        this.addressNew = store.getAddressNew();
        this.operatingTime = store.getOperatingTime();
        this.contact = store.getContact();
        this.siteUrl = store.getSiteUrl();
        this.instagram = store.getInstagram();
        this.reviewCount = reviewCount;
        this.reviewList.addAll(reviewList);
    }

}
