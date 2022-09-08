package com.zeroway.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostRes {
    private Long postId;
    private String username;
    private String userProfileImg;
    private String content;
    private boolean challenge;
    private boolean review;
    private int weeksAgo;
    private int likeCount;
    private int commentCount;
    private boolean liked;
    private boolean bookmarked;
    private List<String> imageList = new ArrayList<>();
    private List<CommentListRes> commentList = new ArrayList<>();

    @QueryProjection
    public PostRes(Long postId, String username, String userProfileImg, String content, boolean challenge, boolean review, int weeksAgo, int likeCount, int commentCount, boolean liked, boolean bookmarked) {
        this.postId = postId;
        this.username = username;
        this.userProfileImg = userProfileImg;
        this.content = content;
        this.weeksAgo = weeksAgo;
        this.challenge = challenge;
        this.review = review;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.liked = liked;
        this.bookmarked = bookmarked;
    }

}
