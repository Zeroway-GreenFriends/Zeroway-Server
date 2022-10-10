package com.zeroway.store.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.common.StatusType;
import com.zeroway.store.dto.StoreReq;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class StoreRequest extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "store_request_id")
    private Long id;

    private Long storeId;

    @Column(length = 100)
    private String name;

    private String item;

    private String operatingTime;

    @Column(length = 13)
    private String contact;

    @Column(length = 100)
    private String siteUrl;

    @Column(length = 100)
    private String instagram;

    @Column(length = 45)
    private String address;

    @Column(length = 45)
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private RequestType type;

    // 삭제 요청
    @Builder
    public StoreRequest(Long storeId, String imageUrl, RequestType type) {
        this.storeId = storeId;
        this.imageUrl = imageUrl;
        this.type = type;
        this.setStatus(StatusType.NEW);
    }

    // 추가, 수정 요청
    public StoreRequest(Long storeId, StoreReq req, @Nullable String imageUrl, RequestType type) {
        this.storeId = storeId;
        this.name = req.getName();
        this.item = req.getItem();
        this.operatingTime = req.getOperatingTime();
        this.contact = req.getContact();
        this.siteUrl = req.getSiteUrl();
        this.instagram = req.getInstagram();
        this.address = req.getAddress();
        this.description = req.getDescription();
        this.imageUrl = imageUrl;
        this.type = type;
        this.setStatus(StatusType.NEW);

    }
}
