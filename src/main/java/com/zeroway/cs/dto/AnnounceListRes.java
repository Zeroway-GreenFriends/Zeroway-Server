package com.zeroway.cs.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnounceListRes {

    private Long announceId;
    private String title;
    private LocalDateTime createdAt;

    public AnnounceListRes(Long announceId, String title, LocalDateTime createdAt) {
        this.announceId = announceId;
        this.title = title;
        this.createdAt = createdAt;
    }
}
