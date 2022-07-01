package com.zeroway.community.dto;

import com.zeroway.community.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentListRes {

    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;

    public CommentListRes(Comment comment) {
        this.userId = comment.getUser().getId();
        this.username = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
