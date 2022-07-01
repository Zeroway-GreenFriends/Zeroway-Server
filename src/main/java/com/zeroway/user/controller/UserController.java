package com.zeroway.user.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.user.dto.SignInReq;
import com.zeroway.user.dto.SignUpReq;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.service.UserService;
import com.zeroway.utils.JwtService;
import com.zeroway.utils.ValidationRegex;
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
     * 회원가입 API
     */
    @PostMapping
    public BaseResponse<PostUserRes> createUser(@RequestBody SignUpReq signUpReq) {
        try {
            PostUserRes postUserRes = userService.join(signUpReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public BaseResponse<PostUserRes> login(@RequestBody SignInReq signInReq) {
        try {
            if (signInReq.getEmail() == null) {
                return new BaseResponse<>(POST_USER_EMPTY_EMAIL);
            }
            if (!isRegexEmail(signInReq.getEmail())) {
                return new BaseResponse<>(POST_USER_INVALID_EMAIL);
            }

            PostUserRes postUserRes = userService.login(signInReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 전체 회원 조회 API
     */
}
