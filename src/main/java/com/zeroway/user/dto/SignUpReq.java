package com.zeroway.user.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpReq {

    @Mapping("email")
    private String email;

    @Mapping("nickname")
    private String nickname;

    private String password;
}
