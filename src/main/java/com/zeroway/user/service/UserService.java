package com.zeroway.user.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.common.BaseResponseStatus;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.dto.SignInReq;
import com.zeroway.user.dto.SignUpReq;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@Transactional(rollbackOn = BaseException.class)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final Mapper mapper;

    /**
     * 회원가입
     */
    public PostUserRes join(SignUpReq signUpReq) throws BaseException {
        User mappedUser = mapper.map(signUpReq, User.class);
        User user = userRepository.save(mappedUser);

        String jwt = jwtService.createRefreshToken(user.getId());
        return PostUserRes.builder()
                .jwt(jwt)
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .challengeCount(user.getChallengeCount())
                .level(user.getLevel())
                .build();
    }

    /**
     * 로그인
     */
    public PostUserRes login(SignInReq signInReq) throws BaseException {
        String email = signInReq.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new BaseException(LOGIN_FAILED);
        }
        User user = userOptional.get();
        if (user.getStatus().equals("inactive")) {
            throw new BaseException(LOGIN_FAILED);
        }
        // jwt 생성
        String jwt = jwtService.createAccessToken(user.getId());

        return PostUserRes.builder()
                .jwt(jwt)
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .challengeCount(user.getChallengeCount())
                .level(user.getLevel())
                .build();
    }
}
