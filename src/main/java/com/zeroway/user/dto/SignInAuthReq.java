package com.zeroway.user.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SignInAuthReq {

    @Mapping("email")
    private String email;

    @Mapping("nickname")
    private String nickname;

    @Mapping("provider")
    private String provider;

}