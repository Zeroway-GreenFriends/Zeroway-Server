package com.zeroway.community.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.user.entity.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class PostLike extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "post_like_id")
    private Long id;

    private Long userId;

    private Long postId;

    @Builder
    public PostLike(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
