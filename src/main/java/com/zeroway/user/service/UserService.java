package com.zeroway.user.service;

import com.zeroway.challenge.ChallengeRepository;
import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.dto.SignInAuthReq;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
//@Transactional(rollbackOn = BaseException.class)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final JwtService jwtService;
    private final Mapper mapper;

    /**
     * 소셜 로그인
     */
    @Transactional
    public PostUserRes authLogin(SignInAuthReq signInReq) throws BaseException {
        String email = signInReq.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        // 존재하지 않은 회원인 경우 -> 회원가입
        User user = mapper.map(signInReq, User.class);

        if (userOptional.isEmpty()) {
            try {
                userRepository.save(user);
                user.setLevel(2);
                List<Long> challengeIds = challengeRepository.findUserChallengeId(user.getId());
                for (Long challengeId : challengeIds) {
                    challengeRepository.insertUserChallenge(challengeId, user.getId());
                }
            }
            catch (Exception e) {
                throw new BaseException(DATABASE_ERROR);
            }
        } else {
            user = userOptional.get();
        }

        if (user.getStatus().equals(StatusType.INACTIVE)) {
            throw new BaseException(LOGIN_FAILED);
        }

        String refreshJwt = jwtService.createRefreshToken(user.getId());
        String accessJwt = jwtService.createAccessToken(user.getId());

        return PostUserRes.builder()
                .accessJwt(accessJwt)
                .refreshJwt(refreshJwt)
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .challengeCount(user.getChallengeCount())
                .level(user.getLevel())
                .build();
    }
}
