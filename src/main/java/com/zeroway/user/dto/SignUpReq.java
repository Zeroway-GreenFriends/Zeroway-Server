package com.zeroway.user.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignUpReq {

    @Mapping("email")
    private String email;

    @Mapping("nickname")
    private String nickname;

    private String password;
}
