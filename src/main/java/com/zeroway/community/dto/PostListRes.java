package com.zeroway.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostListRes {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String userProfileImg;
    private int likeCount;
    private int commentCount;
    private boolean liked;

    private List<String> imageList = new ArrayList<>();

    @QueryProjection
    public PostListRes(Long postId, String title, String content, LocalDateTime createdAt, String username, String userProfileImg, int likeCount, int commentCount, boolean liked) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.username = username;
        this.userProfileImg = userProfileImg;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.liked = liked;
    }
}
