package com.zeroway.challenge.service;

import com.zeroway.challenge.dto.ChallengeCompleteRes;
import com.zeroway.challenge.entity.RedisChallenge;
import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.challenge.dto.ChallengeListRes;
import com.zeroway.challenge.dto.ChallengeRes;
import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.Level;
import com.zeroway.challenge.repository.ChallengeRepository;
import com.zeroway.challenge.repository.RedisChallengeRepository;
import com.zeroway.common.BaseException;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChallengeService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final LevelRepository levelRepository;
    private final RedisChallengeRepository redisChallengeRepository;

    //챌린지 프로필
    public ChallengeRes getList(Long userId) throws BaseException {
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));
            Level userLevel = levelRepository.findById(user.getLevel().getId()).orElseThrow(() -> new BaseException(INVALID_LEVEL_ID));
            return new ChallengeRes(user.getNickname(), user.getLevel().getId(), user.getExp(), userLevel.getImageUrl());
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //오늘의 챌린지
    @Transactional
    public List<ChallengeListRes> getChallenge(Long userId, int size) throws BaseException{
        try {
            Optional<RedisChallenge> optional = redisChallengeRepository.findById(userId);

            //redis에 회원의 오늘의 챌린지가 없는 경우
            if (optional.isEmpty()) {

                //내일 0시
                Calendar tomorrow = new GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                tomorrow.add(Calendar.DAY_OF_MONTH, 1);
                tomorrow.set(Calendar.HOUR_OF_DAY, 0);
                tomorrow.set(Calendar.MINUTE, 0);
                tomorrow.set(Calendar.SECOND, 0);
                tomorrow.set(Calendar.MILLISECOND, 0);

                //redis에 오늘의 챌린지 저장
                redisChallengeRepository.save(RedisChallenge.builder()
                        .userId(userId)
                        .challenge(getTodayChallenge(size))
                        .expiration((tomorrow.getTimeInMillis() - new Date().getTime()) / 1000) //오늘이 지나면 만료
                        .build());
            }
            RedisChallenge redisChallenge = redisChallengeRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));

            return redisChallenge.getChallenge().stream()
                    .map(c -> new ChallengeListRes(
                            c.getId(),
                            challengeRepository.findById(c.getId()).get().getContent(),
                            c.getComplete())
                    )
                    .collect(Collectors.toList());

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //랜덤 챌린지 3개 가져오기
    private List<Long> getTodayChallenge(int size) throws BaseException{
        try {
            LocalDateTime now = LocalDateTime.now();
            List<Challenge> challengeIdList = challengeRepository.findAll();
            List<Long> todayChallengeId = new ArrayList<>();

            Random rn = new Random(now.getDayOfYear());
            for (long i = 0; i < size; i++) {
                int id = rn.nextInt(challengeIdList.size());
                todayChallengeId.add(challengeIdList.get(id).getId());
            }
            return todayChallengeId;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //챌린지 수행/취소
    @Transactional
    public ChallengeCompleteRes patchChallengeComplete(Long userId, Long challengeId, Integer exp) throws Exception{
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));
            RedisChallenge rc = redisChallengeRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));
            List<RedisChallenge.Challenge> c = rc.getChallenge();

            for (RedisChallenge.Challenge challenge : c) {
                if(challenge.getId().equals(challengeId)) {
                    if(challenge.getComplete()) { // 수행 취소
                        challenge.setComplete(false);
                        user.setExp(user.getExp()-exp);
                    }
                    else { // 수행
                        challenge.setComplete(true);
                        user.setExp(user.getExp()+exp);
                    }
                }
            }
            checkLevel(user);
            redisChallengeRepository.save(rc);
            return new ChallengeCompleteRes(user.getLevel().getId());
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //레벨업&다운
    @Transactional
    void checkLevel(User user) throws BaseException {
        if(user.getExp() >= 100) {
            Level levelup = levelRepository.findById(user.getLevel().getId() + 1).orElseThrow(() -> new BaseException(INVALID_LEVEL_ID));
            user.setLevel(levelup);
            user.setExp(user.getExp()-100);

        } else if(user.getExp() < 0){
            if(user.getLevel().getId()!=1) {
                Level levelDown = levelRepository.findById(user.getLevel().getId() - 1).orElseThrow(() -> new BaseException(INVALID_LEVEL_ID));;;
                user.setLevel(levelDown);
                user.setExp(user.getExp()+100);
            } else {
                user.setExp(0);
            }
        }
    }
}