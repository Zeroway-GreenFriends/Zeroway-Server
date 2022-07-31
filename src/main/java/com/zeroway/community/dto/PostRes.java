package com.zeroway.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostRes {
    private Long postId;
    private String username;
    private String userProfileImg;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private boolean liked;
    private boolean bookmarked;
    private List<String> imageList = new ArrayList<>();
    private List<CommentListRes> commentList = new ArrayList<>();

    @QueryProjection
    public PostRes(Long postId, String username, String userProfileImg, String content, LocalDateTime createdAt, int likeCount, int commentCount, boolean liked, boolean bookmarked) {
        this.postId = postId;
        this.username = username;
        this.userProfileImg = userProfileImg;
        this.content = content;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.liked = liked;
        this.bookmarked = bookmarked;
    }

}
