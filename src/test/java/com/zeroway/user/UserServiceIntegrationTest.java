package com.zeroway.user;

import com.github.dozermapper.core.Mapper;
import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.Level;
import com.zeroway.challenge.repository.ChallengeRepository;
import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.user.dto.PatchUserInfo;
import com.zeroway.user.dto.PostUserRes;
import com.zeroway.user.dto.SignInAuthReq;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.user.service.UserService;
import com.zeroway.utils.JwtService;
import com.zeroway.utils.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

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
    RedisService redisService;
    @Mock
    private MockHttpServletRequest request;

    private SignInAuthReq signInAuthReq() {
        return SignInAuthReq.builder()
                .email("test")
                .nickname("예지테스트한다")
                .provider("KAKAO")
                .build();

    }

    // test용 유저
    Optional<User> createUser() {
        return Optional.ofNullable(User.builder()
                .id(1L)
                .email("test")
                .nickname("예지테스트한다")
                .provider(ProviderType.valueOf("KAKAO"))
                .level(levelRepository.findById(1).get())
                .profileImgUrl("https://zeroway.s3.ap-northeast-2.amazonaws.com/userProfile/5b31ec29-854f-4744-85af-384797423fc3_IMG_20220810_162950.jpg")
                .build());
    }

    private Long createRequestJWT() {
        User user = createUser().get();
        userRepository.save(user);
        Long userId = userRepository.findByEmail(user.getEmail()).get().getId();
        String accessToken = jwtService.createAccessToken(userId);

        request = new MockHttpServletRequest();
        request.addHeader("Bearer", accessToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        return userId;
    }

    @DisplayName("로그인 성공")
    @Test
    void loginO() throws BaseException {
        Optional<User> user = createUser();
        user.get().changeLevel(levelRepository.findById(1).get());
        User user1 = userRepository.save(user.get());

        PostUserRes login = userService.login(user.get().getEmail());

        assertThat(login).isNotNull();
        assertThat(login.getRefreshToken()).isEqualTo(redisService.getValues(String.valueOf(user1.getId())));
    }

    @DisplayName("기존 회원 재로그인 시 : 기존 레벨 유지")
    @Test
    void reLoginLevelCheck() throws BaseException {
        SignInAuthReq sign = signInAuthReq();
        User mapUser = mapper.map(sign, User.class);
        MultipartFile multipartFile = null;

        Level twoLevel = levelRepository.findById(2).get();
        mapUser.changeLevel(twoLevel);
        userRepository.save(mapUser);

        PostUserRes login = userService.login(sign.getEmail());

        User user = userRepository.findByEmail(sign.getEmail()).get();
        assertThat(user.getLevel()).isEqualTo(twoLevel);
    }

    @DisplayName("유저 회원가입 성공")
    @Test
    void signInO() throws BaseException {
        SignInAuthReq sign = signInAuthReq();
        User user = mapper.map(sign, User.class);
        MultipartFile multipartFile = null;

        PostUserRes postUserRes = userService.signIn(sign, multipartFile);

        request = new MockHttpServletRequest();
        request.addHeader("Bearer", postUserRes.getAccessToken());
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Long userId = jwtService.getUserIdx();

        assertThat(postUserRes.getRefreshToken()).isEqualTo(redisService.getValues(String.valueOf(userId)));
    }

    @DisplayName("회원 탈퇴 성공: 유저 테이블, redis")
    @Test
    void signoutO() throws BaseException {
        Long userId = this.createRequestJWT();

        Challenge chall1 = challengeRepository.save(Challenge.builder()
                .content("1번 챌린지")
                .build());
        Challenge chall2 = challengeRepository.save(Challenge.builder()
                .content("2번 챌린지")
                .build());

        User signoutUser = userService.signout();

        assertThat(signoutUser.getId()).isEqualTo(userId);
        assertThat(signoutUser.getEmail()).isEqualTo("email@gmail.com");
        assertThat(redisService.getValues(String.valueOf(signoutUser.getId()))).isNull();
    }

    @DisplayName("회원 정보 수정 성공 : 프로필 & 닉네임")
    @Test
    void patchInfo() throws BaseException {
        Long userId = this.createRequestJWT();
        User existedUser = userRepository.findById(userId).get();
        String existedImg = existedUser.getProfileImgUrl();
        String existedNickname = existedUser.getNickname();

        MultipartFile multipartFile = new MockMultipartFile("profile", new byte[5]);
        PatchUserInfo patchUserInfo = new PatchUserInfo("닉넴");
        userService.patchUser(multipartFile, patchUserInfo);

        User resultUser = userRepository.findById(userId).get();
        assertThat(resultUser.getId()).isEqualTo(userId);
        assertThat(resultUser.getNickname()).isEqualTo("닉넴");
        assertThat(resultUser.getProfileImgUrl()).contains("https://zeroway.s3");
        assertThat(resultUser.getProfileImgUrl()).isNotEqualTo(existedImg);
        assertThat(resultUser.getNickname()).isNotEqualTo(existedNickname);
    }

    @DisplayName("회원 정보 수정 성공 : 닉네임만 변경")
    @Test
    void patchInfo1() throws BaseException {
        Long userId = this.createRequestJWT();
        User existedUser = userRepository.findById(userId).get();
        String existedImg = existedUser.getProfileImgUrl();

        MultipartFile multipartFile = new MockMultipartFile("profile", (byte[]) null);
        PatchUserInfo patchUserInfo = new PatchUserInfo("닉넴");
        userService.patchUser(multipartFile, patchUserInfo);

        User resultUser = userRepository.findById(userId).get();
        assertThat(resultUser.getId()).isEqualTo(userId);
        assertThat(resultUser.getNickname()).isEqualTo("닉넴");
        assertThat(resultUser.getProfileImgUrl()).isEqualTo(existedImg);
    }

    @DisplayName("회원 정보 수정 성공 : 프로필 삭제 & 닉네임 변경")
    @Test
    void patchInfo2() throws BaseException {
        Long userId = this.createRequestJWT();
        User existedUser = userRepository.findById(userId).get();
        String existedImg = existedUser.getProfileImgUrl();

        PatchUserInfo patchUserInfo = new PatchUserInfo("닉넴");
        userService.patchUser(null, patchUserInfo);

        User resultUser = userRepository.findById(userId).get();
        assertThat(resultUser.getId()).isEqualTo(userId);
        assertThat(resultUser.getNickname()).isEqualTo("닉넴");
        assertThat(resultUser.getProfileImgUrl()).isNull();
        assertThat(existedImg).contains("https://zeroway.s3");
    }

    @DisplayName("회원 정보 수정 성공 : 프로필만 변경")
    @Test
    void patchInfo3() throws BaseException {
        Long userId = this.createRequestJWT();
        User existedUser = userRepository.findById(userId).get();
        String existedNickname = existedUser.getNickname();

        MultipartFile multipartFile = new MockMultipartFile("profile", new byte[5]);
        userService.patchUser(multipartFile, null);

        User resultUser = userRepository.findById(userId).get();
        assertThat(resultUser.getId()).isEqualTo(userId);
        assertThat(resultUser.getNickname()).isEqualTo(existedNickname);
        assertThat(resultUser.getProfileImgUrl()).isNotNull();
        assertThat(resultUser.getProfileImgUrl()).contains("https://zeroway.s3");
    }

    @DisplayName("닉네임 중복 여부 확인 성공: T")
    @Test
    void nickname() throws BaseException {
        User user = createUser().get();
        user.changeLevel(levelRepository.findById(1).get());
        userRepository.save(user);

        boolean b = userService.existUser(user.getNickname());

        assertThat(b).isTrue();
    }

    @DisplayName("닉네임 중복 여부 확인 성공: T & INACTIVE 유저 제외")
    @Test
    void nickname1() throws BaseException {
        User user = createUser().get();
        user.changeLevel(levelRepository.findById(1).get());
        user = userRepository.save(user);
        user.setStatus(StatusType.INACTIVE);

        boolean b = userService.existUser(user.getNickname());

        assertThat(b).isFalse();
    }

    @DisplayName("닉네임 중복 여부 확인 성공: F")
    @Test
    void nickname2() throws BaseException {
        boolean b = userService.existUser("새로운 닉네임");

        assertThat(b).isFalse();
    }

    @DisplayName("로그아웃 성공")
    @Test
    void logout() throws BaseException {
        Long userId = createRequestJWT();

        userService.logout();
        User user = userRepository.findById(userId).get();

        // status, token
        assertThat(user.getStatus()).isEqualTo(StatusType.LOGOUT);
        assertThat(redisService.getValues(String.valueOf(user.getId()))).isNull();
    }
}