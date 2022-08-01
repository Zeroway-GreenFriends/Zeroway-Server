package com.zeroway.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserInfo {
    private Long userId;
    private String username;
    private String userProfileImg;
    private int level;

    @QueryProjection
    public UserInfo(Long userId, String username, String userProfileImg, int level) {
        this.userId = userId;
        this.username = username;
        this.userProfileImg = userProfileImg;
        this.level = level;
    }
}
