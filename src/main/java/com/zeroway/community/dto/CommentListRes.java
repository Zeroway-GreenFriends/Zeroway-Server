package com.zeroway.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentListRes {

    private Long userId;
    private String username;
    private String userProfileImg;
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;
    private boolean liked;

    @QueryProjection
    public CommentListRes(Long userId, String username, String userProfileImg, Long commentId, String content, LocalDateTime createdAt, int likeCount, boolean liked) {
        this.userId = userId;
        this.username = username;
        this.userProfileImg = userProfileImg;
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.liked = liked;
    }
}
