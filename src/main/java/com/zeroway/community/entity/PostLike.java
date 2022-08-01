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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
