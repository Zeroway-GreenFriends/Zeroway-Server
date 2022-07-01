package com.zeroway.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class PostListRes {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String username;
}
