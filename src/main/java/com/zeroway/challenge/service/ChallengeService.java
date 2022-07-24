package com.zeroway.challenge.service;

import com.zeroway.challenge.dto.ChallengeRes;
import com.zeroway.challenge.repository.ChallengeRepo;
import com.zeroway.challenge.dto.PatchChallengeCompleteRes;
import com.zeroway.common.BaseException;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.REQUEST_ERROR;

@Service
@Slf4j
public class ChallengeService {

    private ChallengeRepo challengeRepo;

    @Autowired
    private UserRepository userRepository;

    public ChallengeService(ChallengeRepo challengeRepo) {
        this.challengeRepo = challengeRepo;
    }

    public ChallengeRes getList(Long userId) throws BaseException {
        try{
            return userRepository.findChallengeRes(userId);
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void completeChallenge(Long userId, Long challengeId) throws Exception{
        try{
            challengeRepo.addChallengeCount(userId);
            int result = challengeRepo.updateChallengeCount(userId, challengeId);
            if(result==0) {
                throw new BaseException(REQUEST_ERROR);
            }
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkLevelUpgrade(Long userId) throws Exception{
        try{

            return challengeRepo.checkLevelUpgrade(userId);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void levelUpgrade(Long userId) {
        challengeRepo.addUserLevel(userId);
        //challenge_count 초기화
        challengeRepo.resetUserChallengeCount(userId);
        //챌린지아이디 찾아와서 하나씩 실행시키기!
        List<Long> challengeIds = challengeRepo.findUserChallengeId(userId);
        for (Long challengeId : challengeIds) {
            challengeRepo.insertUserChallenge(challengeId, userId);
        }
    }

    public PatchChallengeCompleteRes findUserExp(Long userId) throws Exception{
        try{
            return challengeRepo.findUserExp(userId);
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
