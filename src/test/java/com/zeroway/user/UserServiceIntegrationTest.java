package com.zeroway.user;

import com.github.dozermapper.core.Mapper;
import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.Level;
import com.zeroway.challenge.entity.User_Challenge;
import com.zeroway.challenge.repository.ChallengeRepository;
import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.challenge.repository.UserChallengeRepository;
import com.zeroway.common.BaseException;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.dto.SignInAuthReq;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.user.service.UserService;
import com.zeroway.utils.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LevelRepository levelRepository;
    @Autowired
    Mapper mapper;
    @Autowired
    JwtService jwtService;
    @Autowired
    ChallengeRepository challengeRepository;
    @Autowired
    UserChallengeRepository userChallengeRepository;

    private SignInAuthReq signInAuthReq() {
        return SignInAuthReq.builder()
                .email("test")
                .nickname("예지테스트한다")
                .provider("KAKAO")
                .build();

    }

    Optional<User> createUser() {
        return Optional.ofNullable(User.builder()
                .id(1L)
                .refreshToken("yejiReToken")
                .email("test")
                .nickname("예지테스트한다")
                .provider(ProviderType.valueOf("KAKAO"))
                .build());
    }

    @DisplayName("기존 회원 재로그인 시 기존 레벨 유지됨")
    @Test
    void reLoginLevelCheck() throws BaseException {
        SignInAuthReq sign = signInAuthReq();
        User mapUser = mapper.map(sign, User.class);

        Level twoLevel = levelRepository.findById(2).get();
        mapUser.setLevel(twoLevel);
        userRepository.save(mapUser);

        PostUserRes login = userService.login(sign);

        Optional<User> byRefreshToken = userRepository.findByRefreshToken(login.getRefreshToken());
        assertThat(byRefreshToken.get().getLevel()).isEqualTo(twoLevel);
    }

    @DisplayName("유저 회원가입 성공: 유저챌린지 테이블 삽입 확인")
    @Test
    void signInO() throws BaseException {
        User user = createUser().get();
        Level levelOne = levelRepository.findById(1).get();
        Challenge challenge1 = Challenge.builder()
                .content("test")
                .level(levelOne)
                .build();
        Challenge challenge2 = Challenge.builder()
                .content("test2")
                .level(levelOne)
                .build();

        Challenge save1 = challengeRepository.save(challenge1);
        Challenge save2 = challengeRepository.save(challenge2);

        userService.signIn(user, levelOne);

        assertThat(userChallengeRepository.findAll().size()).isEqualTo(2);
        assertThat(userChallengeRepository.findByChallenge_Id(save1.getId()).getUser().getNickname()).isEqualTo(user.getNickname());
        assertThat(userChallengeRepository.findByChallenge_Id(save2.getId()).getUser().getNickname()).isEqualTo(user.getNickname());
        assertThat(userChallengeRepository.findByChallenge_Id(save1.getId()).isComplete()).isFalse();
        assertThat(userChallengeRepository.findByChallenge_Id(save1.getId()).getChallenge().getLevel().getId()).isEqualTo(1);
    }

}
