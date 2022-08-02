package com.zeroway.community.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.user.entity.User;
import lombok.Builder;

import javax.persistence.*;

@Entity
public class CommentLike extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    @Builder
    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
