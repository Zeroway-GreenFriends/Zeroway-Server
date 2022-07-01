package com.zeroway.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUserRes {
    private final String jwt;
    private Long id;
    private String email;
    private String nickname;
    private int challengeCount;
    private int level;
}
