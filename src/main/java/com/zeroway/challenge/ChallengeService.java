package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeRes;
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

    public List<GetChallengeRes> getList(Long userId) throws BaseException {
        try{
            List<GetChallengeRes> result = challengeRepository.getList(userId);
            if(result==null) {
                throw new BaseException(REQUEST_ERROR);
            }
            return challengeRepository.getList(userId);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void completeChallenge(Long userId, Long challengeId) throws Exception{
        try{
            int result = challengeRepository.completeChallenge(userId, challengeId);
            if(result==0) {
                throw new BaseException(REQUEST_ERROR);
            }
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public List<Challenge> findList() {
//        return challengeRepository.findAll();
//    }
}
