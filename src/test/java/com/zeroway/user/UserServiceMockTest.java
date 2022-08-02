package com.zeroway.user;


import com.github.dozermapper.core.DozerBeanMapperBuilder;
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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.EXPIRATION_JWT;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceMockTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private LevelRepository levelRepository;
    @Mock
    private Mapper mapper;

    @Mock
    private JwtService jwtService;

    private final String yejiReToken = "yejiReToken";

    Optional<User> createUser() {
        return Optional.ofNullable(User.builder()
                .id(1L)
                .refreshToken(yejiReToken)
                .email("test")
                .nickname("예지테스트")
                .provider(ProviderType.valueOf("KAKAO"))
                .build());
    }

    @DisplayName("소셜로그인 맵핑")
    @Test
    void signUpMapping() throws BaseException {
        mapper = DozerBeanMapperBuilder.buildDefault();
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

        doReturn(user).when(mapper).map(sign, User.class);

        // 유저가 이미 존재하는 경우
        doReturn(optionalUser).when(userRepository).findByEmail(any(String.class));
        doReturn(optionalUser.get()).when(userRepository).save(any(User.class));

        Level level = new Level();
        level.setId(1);
        level.setImageUrl("11");
        Optional<Level> levelOptional = Optional.ofNullable(level);
        doReturn(levelOptional).when(levelRepository).findById(any());

        doReturn(refreshToken).when(jwtService).createRefreshToken(any());
        doReturn(refreshToken).when(jwtService).createAccessToken(any());

        // when
        PostUserRes res = userService.login(sign);

        // then
        assertThat(res.getRefreshToken()).isEqualTo(optionalUser.get().getRefreshToken());
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void tokenO() throws BaseException {
        Optional<User> user = createUser();

        // given : 리프레시토큰
        when(userRepository.findByRefreshToken(any())).thenReturn(user);

        // when
        String access = userService.refreshToken();

        // then : 액세스토큰
        verify(jwtService, times(1)).expireToken();
        verify(jwtService, times(1)).getToken();
        verify(jwtService, times(1)).createAccessToken(any());
    }
}
