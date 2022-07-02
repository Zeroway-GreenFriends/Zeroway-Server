package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeListRes;
import com.zeroway.challenge.dto.GetChallengeRes;
import com.zeroway.challenge.dto.PatchChallengeCompleteRes;
import com.zeroway.common.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.REQUEST_ERROR;

@Service
@Slf4j
public class ChallengeService {

    private ChallengeRepository challengeRepository;

    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    public GetChallengeRes getList(Long userId) throws BaseException {
        try{
            GetChallengeRes result = challengeRepository.getList(userId);
            if(result==null) {
                throw new BaseException(REQUEST_ERROR);
            }
            return challengeRepository.getList(userId);
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void completeChallenge(Long userId, Long challengeId) throws Exception{
        try{
            challengeRepository.addChallengeCount(userId);
            int result = challengeRepository.updateChallengeCount(userId, challengeId);
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

            return challengeRepository.checkLevelUpgrade(userId);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void levelUpgrade(Long userId) {
        challengeRepository.addUserLevel(userId);
        //challenge_count 초기화
        challengeRepository.resetUserChallengeCount(userId);

        //챌린지아이디 찾아와서 하나씩 실행시키기!



        challengeRepository.insertUserChallenge(userId, userId);
    }

    public PatchChallengeCompleteRes findUserExp(Long userId) throws Exception{
        try{
            return challengeRepository.findUserExp(userId);
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public List<Challenge> findList() {
//        return challengeRepository.findAll();
//    }
}
