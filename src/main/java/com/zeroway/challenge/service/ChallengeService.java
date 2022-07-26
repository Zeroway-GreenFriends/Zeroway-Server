package com.zeroway.challenge.service;

import com.zeroway.challenge.dto.ChallengeListRes;
import com.zeroway.challenge.dto.ChallengeRes;
import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.User_Challenge;
import com.zeroway.challenge.repository.ChallengeRepository;
import com.zeroway.challenge.repository.UserChallengeRepository;
import com.zeroway.common.BaseException;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChallengeService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;


    public ChallengeRes getList(Long userId) throws BaseException {
        try{
            return userRepository.findById(userId)
                    .map(user -> new ChallengeRes(user.getNickname(), user.getLevel().getId(), user.getExp(), user.getProfileImgUrl()))
                    .orElseThrow(IllegalArgumentException::new);
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<ChallengeListRes> getChallengeList(long userId, Integer size) {
        List<Long> challengeIdList = new ArrayList<>();
        List<User_Challenge> userChallenges = new ArrayList<>();

        //user level에 맞는 challenge
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        List<Challenge> challengeList = challengeRepository.findByLevel_Id(user.getLevel().getId());

        //challengeId 랜덤 생성
        for (long i=0; i<size; i++) {
            Long id = (long)(Math.random()*challengeList.size()) + 1;
            while (challengeIdList.contains(id)){
                id = (long)(Math.random()*challengeList.size()) + 1;
            }
            challengeIdList.add(id);
        }

        //user_challenge
        for (Long id : challengeIdList) {
            userChallenges.add(userChallengeRepository.findChallengeList(id, userId));
        }

       return userChallenges.stream()
       .map(uc -> new ChallengeListRes(uc.getChallenge().getId(), uc.getChallenge().getContent(), uc.isComplete()))
       .collect(Collectors.toList());
    }

    public void patchChallengeComplete(Long userId, Long challengeId, Integer exp) throws Exception{
        try{
            User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
            User_Challenge uc = userChallengeRepository.findByUser_IdAndChallenge_Id(userId, challengeId);
            if(!uc.isComplete()) {
                uc.setComplete(true);
                user.setExp(user.getExp()+exp);
            } else {
                uc.setComplete(false);
                user.setExp(user.getExp()-exp);
            }
            userChallengeRepository.save(uc);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
