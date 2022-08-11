package com.zeroway.store.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewInfo {

    private Long reviewId;
    private String username;
    private String userProfileImg;
    private Double score;
    private String content;
    private boolean liked;
    private int likeCount;
    private LocalDateTime createdAt;

    @QueryProjection
    public ReviewInfo(Long reviewId, String username, String userProfileImg, Double score, String content, boolean liked, int likeCount, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.username = username;
        this.userProfileImg = userProfileImg;
        this.score = score;
        this.content = content;
        this.liked = liked;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }
}
