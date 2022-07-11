package com.zeroway.user.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.user.dto.SignInAuthReq;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.service.UserService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.zeroway.common.BaseResponseStatus.*;
import static com.zeroway.utils.ValidationRegex.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 소셜 로그인 API
     */
    @PostMapping("/auth/login")
    public BaseResponse<PostUserRes> authLogin(@RequestBody SignInAuthReq signInReq) {
        try {
            if (signInReq.getEmail() == null) {
                return new BaseResponse<>(POST_USER_EMPTY_EMAIL);
            }
            if (!isRegexEmail(signInReq.getEmail())) {
                return new BaseResponse<>(POST_USER_INVALID_EMAIL);
            }

            PostUserRes postUserRes = userService.authLogin(signInReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * JWT 토큰 재발급
     */

}
