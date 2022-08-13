package com.zeroway.cs.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnounceRes {

    private String title;
    private String content;
    private LocalDateTime createdAt;

    public AnnounceRes(String title, String content, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
