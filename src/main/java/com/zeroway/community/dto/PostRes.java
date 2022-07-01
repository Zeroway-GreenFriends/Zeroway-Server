package com.zeroway.community.dto;

import com.zeroway.community.entity.Comment;
import com.zeroway.community.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostRes {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentListRes> commentList = new ArrayList<>();

    public PostRes(Post post, List<Comment> commentList) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getTitle();
        this.createdAt = post.getCreatedAt();

        for (Comment comment : commentList) {
            this.commentList.add(new CommentListRes(comment));
        }

    }

}
