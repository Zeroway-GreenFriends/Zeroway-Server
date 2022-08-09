package com.zeroway.user.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.user.dto.PostUserAuthLoginReq;
import com.zeroway.user.dto.SignInAuthReq;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.zeroway.common.BaseResponseStatus.*;
import static com.zeroway.utils.ValidationRegex.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 신규 회원 가입 API
     */
    @PostMapping()
    public ResponseEntity<BaseResponse<PostUserRes>> postUser(@RequestPart(value = "signInReq") SignInAuthReq signInReq,
                                                    @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        try {
            if (signInReq.getEmail() == null) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(POST_USER_EMPTY_EMAIL));
            }
            if (!isRegexEmail(signInReq.getEmail())) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(POST_USER_INVALID_EMAIL));
            }

            PostUserRes postUserRes = userService.signIn(signInReq, profileImg);

            return ResponseEntity.ok().body(new BaseResponse<>(postUserRes));
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

    /**
     * 소셜로그인 API - 기존 회원 재로그인
     */
    @PostMapping("/auth/login")
    public ResponseEntity<BaseResponse<PostUserRes>> login(@RequestBody PostUserAuthLoginReq postUserAuthLoginReq) {
        try {
            if (postUserAuthLoginReq.getEmail() == null) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(POST_USER_EMPTY_EMAIL));
            }
            if (!isRegexEmail(postUserAuthLoginReq.getEmail())) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(POST_USER_INVALID_EMAIL));
            }

            PostUserRes postUserRes = userService.login(postUserAuthLoginReq.getEmail());

            return ResponseEntity.ok().body(new BaseResponse<>(postUserRes));
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }


    /**
     * 액세스 토큰 재발급
     */
    @PostMapping("/auth/refresh")
    public ResponseEntity<BaseResponse<String>> authRefresh() {
        try {
            String accessToken = userService.refreshToken();
            return ResponseEntity.ok().body(new BaseResponse<>(accessToken));
        } catch (BaseException e) {
            if (e.getStatus().equals(EXPIRATION_JWT)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(e.getStatus()));
            } else {
                return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
            }
        }
    }

    /**
     * 로그아웃
     */
    @PatchMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        try {
            userService.logout();
            return ResponseEntity.ok().build();
        } catch (BaseException e) {
            e.printStackTrace();
            if (e.getStatus().equals(EXPIRATION_JWT)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(e.getStatus()));
            }
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }
}
