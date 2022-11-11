package com.zeroway.user.controller;

import com.zeroway.common.BaseResponse;
import com.zeroway.user.dto.*;
import com.zeroway.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.zeroway.common.BaseResponseStatus.POST_USER_EMPTY_EMAIL;
import static com.zeroway.common.BaseResponseStatus.POST_USER_INVALID_EMAIL;
import static com.zeroway.utils.ValidationRegex.isRegexEmail;

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
        if (signInReq.getEmail() == null) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(POST_USER_EMPTY_EMAIL));
        }
        if (!isRegexEmail(signInReq.getEmail())) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(POST_USER_INVALID_EMAIL));
        }

        PostUserRes postUserRes = userService.signIn(signInReq, profileImg);

        return ResponseEntity.ok().body(new BaseResponse<>(postUserRes));
    }

    /**
     * 소셜로그인 API - 기존 회원 재로그인
     */
    @PostMapping("/auth/login")
    public ResponseEntity<BaseResponse<PostUserRes>> login(@RequestBody PostUserAuthLoginReq postUserAuthLoginReq) {
        if (postUserAuthLoginReq.getEmail() == null) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(POST_USER_EMPTY_EMAIL));
        }
        if (!isRegexEmail(postUserAuthLoginReq.getEmail())) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(POST_USER_INVALID_EMAIL));
        }

        PostUserRes postUserRes = userService.login(postUserAuthLoginReq.getEmail());

        return ResponseEntity.ok().body(new BaseResponse<>(postUserRes));
    }


    /**
     * 액세스 토큰 재발급
     */
    @PostMapping("/auth/refresh")
    public ResponseEntity<BaseResponse<String>> authRefresh(@RequestBody PostRefreshReq postRefreshReq) {
        String accessToken = userService.refreshToken(postRefreshReq.getEmail());
        return ResponseEntity.ok().body(new BaseResponse<>(accessToken));
    }

    /**
     * 로그아웃
     */
    @PatchMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }

    /**
     * 회원탈퇴
     */
    @PatchMapping("/signout")
    public ResponseEntity<?> signout() {
        userService.signout();
        return ResponseEntity.ok().build();
    }

    /**
     * 회원정보 수정 (닉네임, 프로필 이미지)
     */
    @PatchMapping()
    public ResponseEntity<?> patchUser(@RequestPart(value = "profileImg", required = false) MultipartFile profileImg,
                                   @RequestPart(value = "patchUserInfo", required = false) PatchUserInfo patchUserInfo) {
            userService.patchUser(profileImg, patchUserInfo);
            return ResponseEntity.ok().build();
    }

    /**
     * 닉네임 존재 여부 확인
     */
    @GetMapping("/{nickname}")
    public ResponseEntity<BaseResponse<Boolean>> existUser(@PathVariable String nickname) {
        boolean isExistedUser = userService.existUser(nickname);
        return ResponseEntity.ok().body(new BaseResponse<>(isExistedUser));
    }
}
