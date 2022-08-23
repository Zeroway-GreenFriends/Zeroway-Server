package com.zeroway.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUserRes {
    private final String accessToken;
    private final String refreshToken;
    private String profileImgUrl;
}
