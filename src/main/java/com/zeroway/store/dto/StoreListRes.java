package com.zeroway.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreListRes {

    private Long storeId;
    private String imageUrl;
    private String name;
    private String item;
    private String addressNew;
    private String operatingTime;
    private String contact;
    private String siteUrl;
    private String instagram;

}
