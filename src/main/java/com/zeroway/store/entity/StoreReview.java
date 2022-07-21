package com.zeroway.store.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.user.entity.User;

import javax.persistence.*;

@Entity
public class StoreReview extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // 별점
    // 0.5 ~ 5.0
    private Double score;
}
