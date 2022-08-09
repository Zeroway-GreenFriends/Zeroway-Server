package com.zeroway.store.dto;

import com.zeroway.store.entity.Store;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private static class ReviewInfo {
        private Long reviewId;
        private String username;
        private String userProfileImg;
        private Double score;
        private String content;
        private boolean liked;
        private int likeCount;
        private LocalDateTime createdAt;
    }

    public StoreRes(Store store) {
        this.imageUrl = store.getImageUrl();
        this.name = store.getName();
        this.item = store.getItem();
        this.scoreAvg = store.getScoreAvg();
        this.addressNew = store.getAddressNew();
        this.operatingTime = store.getOperatingTime();
        this.contact = store.getContact();
        this.siteUrl = store.getSiteUrl();
        this.instagram = store.getInstagram();
    }

}
