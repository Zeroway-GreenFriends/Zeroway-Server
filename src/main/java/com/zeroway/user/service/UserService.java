package com.zeroway.user.service;

import com.zeroway.challenge.ChallengeRepository;
import com.zeroway.challenge.LevelRepository;
import com.zeroway.challenge.entity.Level;
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
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
//@Transactional(rollbackOn = BaseException.class)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final ChallengeRepository challengeRepository;
    private final JwtService jwtService;
    private final Mapper mapper;

    /**
     * 소셜 로그인
     */
    @Transactional
    public PostUserRes login(SignInAuthReq signInReq) throws BaseException {
        String email = signInReq.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        // 존재하지 않은 회원인 경우 -> 회원가입
        User user = mapper.map(signInReq, User.class);
        Optional<Level> levelOptional = levelRepository.findById(1);

        if (levelOptional.isEmpty()) {
            throw new BaseException(DATABASE_ERROR);
        } else {
            user.setLevel(levelOptional.get());
        }

        if (userOptional.isEmpty()) {
            try {
                user = userRepository.save(user);
            }
            catch (Exception e) {
                e.printStackTrace();
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

        user.setRefreshToken(refreshJwt);
        userRepository.save(user);


        return PostUserRes.builder()
                .accessToken(accessJwt)
                .refreshToken(refreshJwt)
                .build();
    }


}
