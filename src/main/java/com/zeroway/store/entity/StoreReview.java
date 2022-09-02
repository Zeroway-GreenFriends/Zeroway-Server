package com.zeroway.store.entity;

import com.zeroway.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Deprecated
@Entity
@NoArgsConstructor
@Getter
public class StoreReview extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private Long storeId;

    private Long userId;

    // 별점
    // 0.5 ~ 5.0
    private Double score;

    @Column(length = 1000)
    private String content;

    @Builder
    public StoreReview(Long storeId, Long userId, Double score, String content) {
        this.storeId = storeId;
        this.userId = userId;
        this.score = score;
        this.content = content;
    }
}
