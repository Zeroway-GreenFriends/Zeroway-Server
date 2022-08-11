package com.zeroway.community.entity;

import com.zeroway.common.BaseEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class CommentLike extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "comment_like_id")
    private Long id;

    private Long userId;

    private Long commentId;


    @Builder
    public CommentLike(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
