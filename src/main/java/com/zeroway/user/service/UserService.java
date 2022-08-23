package com.zeroway.user.service;

import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.User_Challenge;
import com.zeroway.challenge.repository.ChallengeRepository;
import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.challenge.entity.Level;
import com.zeroway.challenge.repository.UserChallengeRepository;
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
import java.util.List;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@Transactional(rollbackOn = BaseException.class)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final JwtService jwtService;
    private final Mapper mapper;
    private final S3Uploader s3Uploader;

    /**
     * 소셜 로그인
     */
    public PostUserRes login(String email) throws BaseException {
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
    public PostUserRes signIn(SignInAuthReq signInAuthReq, MultipartFile profileImg) throws BaseException {
        Optional<User> optionalUser = userRepository.findByEmail(signInAuthReq.getEmail());
        if (optionalUser.isPresent()) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        User user = mapper.map(signInAuthReq, User.class);

        // 레벨 1
        Optional<Level> levelOptional = levelRepository.findById(1);
        user.setLevel(levelOptional.get());

        try {
            if (profileImg != null && !profileImg.isEmpty()) {
                String userProfileUrl = s3Uploader.uploadFile(profileImg, "userProfile");
                user.setProfileImgUrl(userProfileUrl);
            }
        } catch (IOException e) {
            throw new BaseException(FILE_UPLOAD_ERROR);
        }

        user = userRepository.save(user);

        // userChallengeRepo 데이터 삽입 : 레벨1 챌린지
        List<Challenge> levelOneChallenge = challengeRepository.findByLevel_Id(levelOptional.get().getId());

        for (int i = 0; i < levelOneChallenge.size(); i++) {
            User_Challenge userChallenge = User_Challenge.builder()
                    .user(user)
                    .challenge(levelOneChallenge.get(i))
                    .build();
            userChallengeRepository.save(userChallenge);
        }

        return postUser(user);
    }

    /**
     * 토큰 두종류 발급
     */
    private PostUserRes postUser(User user) throws BaseException {
        String refreshJwt = jwtService.createRefreshToken(user.getId());
        String accessJwt = jwtService.createAccessToken(user.getId());

        user.setRefreshToken(refreshJwt);
        user.setStatus(StatusType.ACTIVE);
        userRepository.save(user);

        return PostUserRes.builder()
                .refreshToken(refreshJwt)
                .accessToken(accessJwt)
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

    /**
    액세스토큰 재발급
     */
    public String refreshToken() throws BaseException {
        jwtService.expireToken();
        String refreshToken = jwtService.getToken();

        Optional<User> optionalUser = userRepository.findByRefreshToken(refreshToken);
        Long userIdx;

        if (optionalUser.isEmpty()) {
            throw new BaseException(REQUEST_ERROR);
        } else {
            userIdx = optionalUser.get().getId();
        }

        return jwtService.createAccessToken(userIdx);
    }

    /**
     * 로그아웃
     */
    public void logout() throws BaseException {
        try {
            // 토큰 만료 확인
            Long userIdx = jwtService.getUserIdx();
            User user = userRepository.findById(userIdx).get();
            user.setStatus(StatusType.LOGOUT);
            user.setRefreshToken(null);
        } catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 회원탈퇴
     */
    public User signout() throws BaseException {
        try {
            Long userIdx = jwtService.getUserIdx();
            User user = userRepository.findById(userIdx).get();

            user.signout("알 수 없음", "email@gmail.com", null, null, levelRepository.findById(1).get(), StatusType.INACTIVE);
            userChallengeRepository.deleteByUser_Id(userIdx);
            return user;
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 회원정보 수정
     */
    public void patchUser(MultipartFile profileImg, PatchUserInfo patchUserInfo) throws BaseException {
        User user = null;
        try {
            Long userIdx = jwtService.getUserIdx();
            user = userRepository.findById(userIdx).get();

            if (patchUserInfo != null) {
                String nickname = patchUserInfo.getNickname();
                user.setNickname(nickname);
            }

            if (profileImg == null) {
                user.setProfileImgUrl(null);
            } else if (!profileImg.isEmpty()) {
                String userProfile = s3Uploader.uploadFile(profileImg, "userProfile");
                user.setProfileImgUrl(userProfile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(FILE_UPLOAD_ERROR);
        } catch (BaseException e) {
            e.printStackTrace();
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
