package com.zeroway.user;

import com.zeroway.common.BaseException;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.utils.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
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
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.EXPIRATION_JWT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Spy
    @InjectMocks
    private JwtService jwtService = new JwtService();

    @Mock
    private MockHttpServletRequest request;

    @Test
    @DisplayName("만료된 토큰 검증")
    void tokenX() throws BaseException, InterruptedException {
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
                .refreshToken(refreshToken)
                .email("test")
                .nickname("t")
                .provider(ProviderType.valueOf("KAKAO"))
                .build());

        doReturn(bearer).when(jwtService).getToken();

        // when
        BaseException baseException = Assertions.assertThrows(BaseException.class, () -> jwtService.expireToken());

        // then
        assertThat(baseException.getStatus()).isEqualTo(EXPIRATION_JWT);
    }
}