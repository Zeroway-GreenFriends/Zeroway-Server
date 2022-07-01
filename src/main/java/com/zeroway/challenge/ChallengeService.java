package com.zeroway.challenge;

import com.zeroway.challenge.entity.Challenge;
import com.zeroway.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    public List<Challenge> findList() {
        return challengeRepository.findAll();
    }
}
