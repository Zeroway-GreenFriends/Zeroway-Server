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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
//@Transactional(rollbackOn = BaseException.class)
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
    @Transactional
    public PostUserRes login(SignInAuthReq signInReq, MultipartFile profileImg) throws BaseException {
        String email = signInReq.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        // 존재하지 않은 회원인 경우 -> 회원가입
        User user = mapper.map(signInReq, User.class);
        Optional<Level> levelOptional = levelRepository.findById(1);
        boolean newUser = false;

        if (levelOptional.isEmpty()) {
            throw new BaseException(DATABASE_ERROR);
        } else {
            if (userOptional.isEmpty()) {
                try {
                    Level levelOne = levelOptional.get();
                    signIn(user, levelOne, profileImg);
                    newUser = true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw new BaseException(DATABASE_ERROR);
                }
            } else {
                user = userOptional.get();
            }
        }

        if (user.getStatus().equals(StatusType.INACTIVE)) {
            throw new BaseException(LOGIN_FAILED);
        }

        String refreshJwt = jwtService.createRefreshToken(user.getId());
        String accessJwt = jwtService.createAccessToken(user.getId());

        user.setRefreshToken(refreshJwt);
        user.setStatus(StatusType.ACTIVE);
        userRepository.save(user);

        return PostUserRes.builder()
                .accessToken(accessJwt)
                .refreshToken(refreshJwt)
                .newUser(newUser)
                .build();
    }

    /**
     * 신규 회원가입
     */
    public void signIn(User user, Level levelOne, MultipartFile profileImg) throws BaseException {
        user.setLevel(levelOne);
        try {
            if (profileImg != null && !profileImg.isEmpty()) {
                String userProfileUrl = s3Uploader.uploadFile(profileImg, "userProfile");
                user.setProfileImgUrl(userProfileUrl);
            }

            user = userRepository.save(user);
            List<Challenge> levelOneChallenge = challengeRepository.findByLevel_Id(levelOne.getId());

            for (int i = 0; i < levelOneChallenge.size(); i++) {
                User_Challenge userChallenge = User_Challenge.builder()
                        .user(user)
                        .challenge(levelOneChallenge.get(i))
                        .build();
                userChallengeRepository.save(userChallenge);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
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
            userRepository.save(user);
        } catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
