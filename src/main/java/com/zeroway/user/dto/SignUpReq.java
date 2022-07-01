package com.zeroway.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpReq {
    private String email;
    private String nickname;
    private String password;
}
