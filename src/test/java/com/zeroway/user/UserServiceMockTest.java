package com.zeroway.user;


import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.zeroway.challenge.entity.Level;
import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.dto.SignInAuthReq;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.user.service.UserService;
import com.zeroway.utils.JwtService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;
import static com.zeroway.common.StatusType.INACTIVE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
                .email("test@test")
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

    @DisplayName("회원가입 실패: 이미 존재 하는 유저")
    @Test
    void signUpX() throws BaseException {
        SignInAuthReq sign = signInAuthReq();
        MultipartFile multipartFile = null;
        Optional<User> optionalUser = createUser();

        doReturn(optionalUser).when(userRepository).findByEmail(any());

        BaseException baseException = assertThrows(BaseException.class, () -> userService.signIn(sign, multipartFile));

        assertThat(baseException.getStatus()).isEqualTo(POST_USERS_EXISTS_EMAIL);
    }

    @DisplayName("로그인 실패: 존재하지 않는 유저")
    @Test
    void loginX() throws BaseException {
        SignInAuthReq sign = signInAuthReq();
        Optional<User> user = Optional.empty();
        doReturn(user).when(userRepository).findByEmail(any());

        BaseException baseException = assertThrows(BaseException.class, () -> userService.login(sign.getEmail()));

        assertThat(baseException.getStatus()).isEqualTo(LOGIN_FAILED);
    }

    @DisplayName("로그인 실패: 탈퇴한 유저")
    @Test
    void loginX1() throws BaseException {
        Optional<User> optionalUser = createUser();
        optionalUser.get().setStatus(INACTIVE);
        doReturn(optionalUser).when(userRepository).findByEmail(any());

        BaseException baseException = assertThrows(BaseException.class, () -> userService.login(optionalUser.get().getEmail()));

        assertThat(baseException.getStatus()).isEqualTo(POST_USER_INACTIVE);
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

    @DisplayName("로그아웃 성공")
    @Test
    void logoutO() throws BaseException {
        Optional<User> user = createUser();
        assertThat(user.get().getStatus()).isEqualTo(StatusType.ACTIVE);

        doReturn(user.get().getId()).when(jwtService).getUserIdx();
        doReturn(user).when(userRepository).findById(any());

        userService.logout();
        assertThat(user.get().getStatus()).isEqualTo(StatusType.LOGOUT);
    }
}