package com.zeroway.store.dto;

import com.zeroway.store.entity.Store;
import lombok.Data;

import java.util.List;

/**
 * 제로웨이스트 샵 상세 조회 응답 데이터
 */
@Data
public class StoreRes {
    private String imageUrl;
    private String name;
    private String item;
    private String addressNew;
    private String operatingTime;
    private String contact;
    private String siteUrl;
    private String instagram;
    private String description;


    public StoreRes(Store store) {
        this.imageUrl = store.getImageUrl();
        this.name = store.getName();
        this.item = store.getItem();
        this.addressNew = store.getAddressNew();
        this.operatingTime = store.getOperatingTime();
        this.contact = store.getContact();
        this.siteUrl = store.getSiteUrl();
        this.instagram = store.getInstagram();
        this.description = store.getDescription();
    }

}
