package com.zeroway.store.entity;

import com.zeroway.common.BaseEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class StoreReviewLike extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "store_review_like_id")
    private Long id;

    private Long userId;

    private Long storeReviewId;


    @Builder
    public StoreReviewLike(Long userId, Long storeReviewId) {
        this.userId = userId;
        this.storeReviewId = storeReviewId;
    }
}
