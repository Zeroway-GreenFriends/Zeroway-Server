package com.zeroway.user.service;

import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.challenge.entity.Level;
import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.s3.S3Uploader;
import com.zeroway.user.dto.PatchUserInfo;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.dto.SignInAuthReq;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@Transactional(rollbackOn = BaseException.class)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final JwtService jwtService;
    private final Mapper mapper;
    private final S3Uploader s3Uploader;

    /**
     * 소셜 로그인
     */
    public PostUserRes login(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BaseException(LOGIN_FAILED);
        } else {
            User user = userOptional.get();

            if (user.getStatus().equals(StatusType.INACTIVE)) {
                throw new BaseException(POST_USER_INACTIVE);
            }

            return postUser(user);
        }
    }

    /**
     * 신규 회원가입
     */
    public PostUserRes signIn(SignInAuthReq signInAuthReq, MultipartFile profileImg) {
        Optional<User> optionalUser = userRepository.findByEmail(signInAuthReq.getEmail());
        if (optionalUser.isPresent()) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        User user = mapper.map(signInAuthReq, User.class);

        // 레벨 1
        Optional<Level> levelOptional = levelRepository.findById(1);
        if (levelOptional.isPresent()) {
            user.changeLevel(levelOptional.get());
        }

        try {
            if (profileImg != null && !profileImg.isEmpty()) {
                String userProfileUrl = s3Uploader.uploadFile(profileImg, "userProfile");
                user.uploadProfileImg(userProfileUrl);
            }
        } catch (IOException e) {
            throw new BaseException(FILE_UPLOAD_ERROR);
        }

        user = userRepository.save(user);
        return postUser(user);
    }

    /**
     * 토큰 두종류 발급
     */
    private PostUserRes postUser(User user) {
        String refreshJwt = jwtService.createRefreshToken(user.getId());
        String accessJwt = jwtService.createAccessToken(user.getId());

        user.setStatus(StatusType.ACTIVE);

        return PostUserRes.builder()
                .refreshToken(refreshJwt)
                .accessToken(accessJwt)
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

    /**
    액세스토큰 재발급
     */
    public String refreshToken(String email) {
        // refresh 만료 확인
        jwtService.expireToken();
        String refreshToken = jwtService.getToken();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        Long userIdx;

        if (optionalUser.isEmpty()) {
            throw new BaseException(REQUEST_ERROR);
        } else {
            userIdx = optionalUser.get().getId();
            // redis 토큰값 일치 확인
            jwtService.checkRefreshTokenByRedis(userIdx, refreshToken);
        }

        return jwtService.createAccessToken(userIdx);
    }

    /**
     * 로그아웃
     */
    public void logout() {
        try {
            // 토큰 만료 확인
            Long userIdx = jwtService.getUserIdx();
            User user = null;
            Optional<User> optionalUser = userRepository.findById(userIdx);
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            }
            user.setStatus(StatusType.LOGOUT);
            jwtService.deleteRefreshToken(userIdx);
        } catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 회원탈퇴
     */
    public void signout() {
        try {
            Long userIdx = jwtService.getUserIdx();
            Optional<User> optionalUser = userRepository.findById(userIdx);
            Optional<Level> levelOptional = levelRepository.findById(1);
            if (optionalUser.isPresent() && levelOptional.isPresent()) {
                User user = optionalUser.get();

                user.signout(levelOptional.get());
                jwtService.deleteRefreshToken(userIdx);
            }
        } catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 회원정보 수정
     */
    public void patchUser(MultipartFile profileImg, PatchUserInfo patchUserInfo) {
        try {
            Long userIdx = jwtService.getUserIdx();
            Optional<User> optionalUser = userRepository.findById(userIdx);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (patchUserInfo != null) {
                    String nickname = patchUserInfo.getNickname();
                    user.changeNickname(nickname);
                }

                if (profileImg == null) {
                    user.uploadProfileImg(null);
                } else if (!profileImg.isEmpty()) {
                    String userProfile = s3Uploader.uploadFile(profileImg, "userProfile");
                    user.uploadProfileImg(userProfile);
                }
            }
        } catch (IOException e) {
            throw new BaseException(FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 닉네임 중복 여부 확인
     */
    public boolean existUser(String nickname) {
        if (nickname.equals("알 수 없음")) {
            return true;
        }
        return userRepository.existsUserByNicknameAndStatusNot(nickname, StatusType.INACTIVE);
    }
}
