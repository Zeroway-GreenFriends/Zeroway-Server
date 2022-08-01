package com.zeroway.utils;

import com.zeroway.common.BaseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Date;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
public class JwtService {

    private final int accessTokenMs = 1000 * 60 * 60;   // 1시간
    private final int refreshTokenMs = 1000 * 60 * 60 * 24 * 7;    // 1주

    private final static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    private final static Key key = Keys.secretKeyFor(signatureAlgorithm);

    /**
     * JWT 생성
     */
    public String createAccessToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx", userId)
                .setSubject(userId.toString())
                .setExpiration(new Date(now.getTime() + accessTokenMs))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .setExpiration(new Date(now.getTime() + refreshTokenMs))
                .signWith(key)
                .compact();
    }

    public String createExpiredTokenTest() {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setExpiration(new Date(now.getTime() + 1000))  // 1초
                .signWith(key)
                .compact();
    }

    /**
     * Header에서 Bearer으로 JWT 추출
     */
    public String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Bearer");
    }

    /**
     * 토큰 만료 응답 or accessToken에서 userIdx 추출 (유효한 토큰 시)
     */
    public Long getUserIdx() throws BaseException {

        Jws<Claims> claims = expireToken();

        // userIdx 추출
        return claims.getBody().get("userIdx",Long.class);
    }

    /**
     * 토큰 만료 확인
     */
    public Jws<Claims> expireToken() throws BaseException {
        // 1. JWT 추출
        String token = getToken();
        if (token == null || token.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key.getEncoded())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new BaseException(EXPIRATION_JWT);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            throw new BaseException(INVALID_JWT);
        }

        return claims;
    }
}
