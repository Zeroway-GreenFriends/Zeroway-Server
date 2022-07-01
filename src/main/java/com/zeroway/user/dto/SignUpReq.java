package com.zeroway.user.dto;

import com.github.dozermapper.core.Mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReq {

    @Mapping("email")
    private String email;

    @Mapping("nickname")
    private String nickname;

    private String password;
}
