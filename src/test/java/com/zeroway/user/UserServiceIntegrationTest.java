package com.zeroway.user;

import com.github.dozermapper.core.Mapper;
import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.Level;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Mock
    private MockHttpServletRequest request;

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

    @DisplayName("로그인 성공")
    @Test
    void loginO() throws BaseException {
        Optional<User> user = createUser();
        user.get().setLevel(levelRepository.findById(1).get());
        userRepository.save(user.get());

        PostUserRes login = userService.login(user.get().getEmail());

        assertThat(login).isNotNull();
        assertThat(user.get().getEmail()).isEqualTo(userRepository.findByRefreshToken(login.getRefreshToken()).get().getEmail());
    }

    @DisplayName("기존 회원 재로그인 시 : 기존 레벨 유지")
    @Test
    void reLoginLevelCheck() throws BaseException {
        SignInAuthReq sign = signInAuthReq();
        User mapUser = mapper.map(sign, User.class);
        MultipartFile multipartFile = null;

        Level twoLevel = levelRepository.findById(2).get();
        mapUser.setLevel(twoLevel);
        userRepository.save(mapUser);

        PostUserRes login = userService.login(sign.getEmail());

        Optional<User> byRefreshToken = userRepository.findByRefreshToken(login.getRefreshToken());
        assertThat(byRefreshToken.get().getLevel()).isEqualTo(twoLevel);
    }

    @DisplayName("유저 회원가입 성공: 유저챌린지 테이블 삽입 확인")
    @Test
    void signInO() throws BaseException {
        SignInAuthReq sign = signInAuthReq();
        User user = mapper.map(sign, User.class);
        user.setRefreshToken("yejiReToken");
        MultipartFile multipartFile = null;

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

        userService.signIn(sign, multipartFile);

        assertThat(userChallengeRepository.findByChallenge_Id(save1.getId()).getUser().getNickname()).isEqualTo(user.getNickname());
        assertThat(userChallengeRepository.findByChallenge_Id(save2.getId()).getUser().getNickname()).isEqualTo(user.getNickname());
        assertThat(userChallengeRepository.findByChallenge_Id(save1.getId()).isComplete()).isFalse();
        assertThat(userChallengeRepository.findByChallenge_Id(save1.getId()).getChallenge().getLevel().getId()).isEqualTo(1);
    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    void signoutO() throws BaseException {
        Long userId = userRepository.findByEmail("testYeji@test.com").get().getId();
        String accessToken = jwtService.createAccessToken(userId);

        request = new MockHttpServletRequest();
        request.addHeader("Bearer", accessToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        User signoutUser = userService.signout();

        assertThat(signoutUser.getId()).isEqualTo(userId);
        assertThat(signoutUser.getEmail()).isEqualTo("email@gmail.com");
        assertThat(signoutUser.getRefreshToken()).isNull();
    }
}