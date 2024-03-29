package com.zeroway.user;

import com.zeroway.common.BaseException;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.utils.JwtService;
import com.zeroway.utils.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.EXPIRATION_JWT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Spy
    @InjectMocks
    private JwtService jwtService;

    @Mock
    private MockHttpServletRequest request;
    @Mock
    private RedisService redisService;

    @Test
    @DisplayName("만료된 토큰 검증")
    void tokenX() throws InterruptedException {
        // given
        String refreshToken = jwtService.createExpiredTokenTest();

        Thread.sleep(2000l);

        request = new MockHttpServletRequest();
        request.addHeader("Bearer", refreshToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String bearer = request.getHeader("Bearer");

        Optional<User> user = Optional.ofNullable(User.builder()
                .id(1L)
                .email("test")
                .nickname("t")
                .provider(ProviderType.valueOf("KAKAO"))
                .build());

        doReturn(bearer).when(jwtService).getToken();

        // when
        BaseException baseException = assertThrows(BaseException.class, () -> jwtService.expireToken());

        // then
        assertThat(baseException.getStatus()).isEqualTo(EXPIRATION_JWT);
    }
}