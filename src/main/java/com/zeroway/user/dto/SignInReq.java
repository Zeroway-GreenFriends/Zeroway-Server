package com.zeroway.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignInReq {
    private String email;
    private String nickname;
}