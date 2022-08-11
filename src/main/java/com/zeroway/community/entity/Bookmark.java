package com.zeroway.community.entity;

import com.zeroway.common.BaseEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Bookmark extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;

    private Long userId;

    private Long postId;

    @Builder
    public Bookmark(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
