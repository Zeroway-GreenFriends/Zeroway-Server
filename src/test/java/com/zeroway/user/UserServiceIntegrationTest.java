package com.zeroway.user;

import com.github.dozermapper.core.Mapper;
import com.zeroway.challenge.entity.Level;
import com.zeroway.challenge.repository.LevelRepository;
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

    @DisplayName("기존 회원 재로그인시 기존 레벨 유지됨")
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

}
