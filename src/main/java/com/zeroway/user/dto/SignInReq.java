package com.zeroway.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInReq {
    private String email;
    private String nickname;
}
