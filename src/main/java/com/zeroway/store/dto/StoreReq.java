package com.zeroway.store.dto;

import com.zeroway.store.entity.RequestType;
import lombok.Data;

@Data
public class StoreReq {

    private String name;
    private String item;
    private String operatingTime;
    private String contact;
    private String siteUrl;
    private String instagram;
    private String address;
    private String description;
    private String imageUrl;
    private RequestType type;
}
