package com.zeroway.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignInAuthReq {
    private String email;
    private String nickname;
}