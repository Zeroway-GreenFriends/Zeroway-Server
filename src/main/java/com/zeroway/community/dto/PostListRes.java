package com.zeroway.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostListRes {

    private Long postId;
    private String content;
    private boolean challenge;
    private boolean review;
    private String username;
    private String userProfileImg;
    private int likeCount;
    private int commentCount;
    private boolean liked;
    private boolean bookmarked;

    private List<String> imageList = new ArrayList<>();

    @QueryProjection
    public PostListRes(Long postId, String content, boolean challenge, boolean review, String username, String userProfileImg, int likeCount, int commentCount, boolean liked, boolean bookmarked) {
        this.postId = postId;
        this.content = content;
        this.challenge = challenge;
        this.review = review;
        this.username = username;
        this.userProfileImg = userProfileImg;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.liked = liked;
        this.bookmarked = bookmarked;
    }
}
