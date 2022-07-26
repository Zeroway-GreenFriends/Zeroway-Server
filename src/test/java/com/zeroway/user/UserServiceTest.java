package com.zeroway.user;


import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.zeroway.common.BaseException;
import com.zeroway.user.config.DozerMapperConfig;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.dto.SignInAuthReq;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.user.service.UserService;
import com.zeroway.utils.JwtService;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    private Mapper mapper;

    @Spy
    private JwtService jwtService;

    @BeforeEach
    public void before() throws Exception {
        mapper = DozerBeanMapperBuilder.buildDefault();
    }

    @DisplayName("소셜로그인 맵핑")
    @Test
    void signUpMapping() throws BaseException {
        jwtService = new JwtService();

        // given
        SignInAuthReq request = signInAuthReq();
        User user = mapper.map(request, User.class);

        assertThat(user.getEmail()).isEqualTo(request.getEmail());

        String refreshToken = jwtService.createRefreshToken(user.getId());
        user = User.builder()
                .refreshToken(refreshToken)
                .build();

        assertThat(user.getRefreshToken()).isEqualTo(refreshToken);
    }

    private SignInAuthReq signInAuthReq() {
        return SignInAuthReq.builder()
                .email("test@test")
                .nickname("예지테스트한다")
                .provider("KAKAO")
                .build();

    }

    @DisplayName("소셜로그인 성공")
    @Test
    void signUpO() throws BaseException {
        // given
        SignInAuthReq sign = signInAuthReq();

        // userId 임의로 설정
        String refreshToken = jwtService.createRefreshToken(1L);

        User user = User.builder()
                .email(sign.getEmail())
                .nickname(sign.getNickname())
                .provider(ProviderType.valueOf(sign.getProvider()))
                .build();

        Optional<User> optionalUser = Optional.ofNullable(User.builder()
                .email(sign.getEmail())
                .nickname(sign.getNickname())
                .provider(ProviderType.valueOf(sign.getProvider()))
                .refreshToken(refreshToken)
                .build());

        // 유저가 이미 존재하는 경우
        doReturn(optionalUser).when(userRepository).findByEmail(any(String.class));
        doReturn(optionalUser).when(userRepository).save(any(User.class));

        // when
//        PostUserRes res = userService.login(sign);

        // then
//        assertThat(res.getRefreshToken()).isEqualTo(optionalUser.get().getRefreshToken());
    }

    @DisplayName("회원가입 실패: 이미 가입된 이메일")
    @Test
    void signUpX() {

    }
}
