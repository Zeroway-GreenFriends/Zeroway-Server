package com.zeroway.challenge.service;

import com.zeroway.challenge.dto.ChallengeCompleteRes;
import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.challenge.dto.ChallengeListRes;
import com.zeroway.challenge.dto.ChallengeRes;
import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.Level;
import com.zeroway.challenge.entity.User_Challenge;
import com.zeroway.challenge.repository.ChallengeRepository;
import com.zeroway.challenge.repository.UserChallengeRepository;
import com.zeroway.common.BaseException;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChallengeService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final LevelRepository levelRepository;


    public ChallengeRes getList(Long userId) throws BaseException {
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));
            Level userLevel = levelRepository.findById(user.getLevel().getId()).orElseThrow(() -> new BaseException(INVALID_LEVEL_ID));

            return new ChallengeRes(user.getNickname(), user.getLevel().getId(), user.getExp(), userLevel.getImageUrl());
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<ChallengeListRes> getTodayChallengeList(long userId, Integer size) throws BaseException{
        List<User_Challenge> todayChallenge = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));

        //오늘의 챌린지 선별
        LocalDateTime todayLdt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        List<User_Challenge> challenges = userChallengeRepository.findTodayChallenge(user.getId(), user.getLevel().getId(), todayLdt);

        Random rn = new Random(now.getDayOfYear());
        for(long i=0; i<size; i++) {
            int id = rn.nextInt(challenges.size());
            while(todayChallenge.contains(challenges.get(id))){
                id = rn.nextInt(challenges.size());
            }
            todayChallenge.add(challenges.get(id));
        }

        return todayChallenge.stream()
                .map(uc -> new ChallengeListRes(uc.getChallenge().getId(), uc.getChallenge().getContent(), uc.isComplete()))
                .collect(Collectors.toList());
    }

    public List<ChallengeListRes> getChallengeList(Long userId) throws BaseException{
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));
        List<User_Challenge> challengeList = userChallengeRepository.findByChallengeList(userId, user.getLevel().getId());

        return challengeList.stream()
                .map(uc -> new ChallengeListRes(uc.getChallenge().getId(), uc.getChallenge().getContent(), uc.isComplete()))
                .collect(Collectors.toList());
    }

    public ChallengeCompleteRes patchChallengeComplete(Long userId, Long challengeId, Integer exp) throws Exception{
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));
            User_Challenge uc = userChallengeRepository.findByUser_IdAndChallenge_Id(userId, challengeId);
            if(!uc.isComplete()) {
                uc.setComplete(true);
                user.changeExp(user.getExp()+exp);
            } else {
                uc.setComplete(false);
                user.changeExp(user.getExp()-exp);
            }
            checkLevel(user);
            userChallengeRepository.save(uc);
            return new ChallengeCompleteRes(user.getLevel().getId());
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //레벨업&다운
    private void checkLevel(User user) throws BaseException {
        if(user.getExp() >= 100) {
            Level levelup = levelRepository.findById(user.getLevel().getId() + 1).orElseThrow(() -> new BaseException(INVALID_LEVEL_ID));
            user.changeLevel(levelup);
            user.changeExp(user.getExp()-100);
            createUC(user);

        } else if(user.getExp() < 0){
            if(user.getLevel().getId()!=1) {
                Level levelDown = levelRepository.findById(user.getLevel().getId() - 1).orElseThrow(() -> new BaseException(INVALID_LEVEL_ID));;;
                user.changeLevel(levelDown);
                user.changeExp(user.getExp()+100);
            } else {
                user.changeExp(0);
            }
        }
        userRepository.save(user);
    }

    private void createUC(User user) {
        List<Challenge> challenges = challengeRepository.findByLevel_Id(user.getLevel().getId());
        if(userChallengeRepository.findByUser_IdAndChallenge_Id(user.getId(), challenges.get(0).getId())==null) {
            for (Challenge challenge : challenges) {
                userChallengeRepository.save(User_Challenge.builder()
                        .user(user).challenge(challenge).build());
            }
        }
    }

}