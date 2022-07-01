package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeRes;
import com.zeroway.common.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ChallengeService {

    private ChallengeRepository challengeRepository;

    public List<GetChallengeRes> getList(int userId) throws BaseException {

        try{
            return challengeRepository.getList(userId);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public List<Challenge> findList() {
//        return challengeRepository.findAll();
//    }
}
