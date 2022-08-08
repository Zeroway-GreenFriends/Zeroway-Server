package com.zeroway.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreListRes {

    private String imageUrl;
    private String name;
    private String item;
    private Double scoreAvg;
    private String addressNew;
    private String operatingTime;
    private String contact;
    private String siteUrl;
    private String instagram;

}
