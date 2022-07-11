package com.zeroway.user.dto;

import com.github.dozermapper.core.Mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReq {

    @Mapping("email")
    private String email;

    @Mapping("password")
    private String password;

    @Mapping("nickname")
    private String nickname;
}
