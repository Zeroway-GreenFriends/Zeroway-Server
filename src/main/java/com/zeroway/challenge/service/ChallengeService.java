package com.zeroway.challenge.service;

import com.zeroway.challenge.dto.ChallengeListRes;
import com.zeroway.challenge.dto.ChallengeRes;
import com.zeroway.challenge.entity.Challenge;
import com.zeroway.challenge.entity.User_Challenge;
import com.zeroway.challenge.repository.ChallengeRepo;
import com.zeroway.challenge.dto.PatchChallengeCompleteRes;
import com.zeroway.challenge.repository.ChallengeRepository;
import com.zeroway.challenge.repository.UserChallengeRepository;
import com.zeroway.common.BaseException;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.REQUEST_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepo challengeRepo;
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;


    public ChallengeRes getList(Long userId) throws BaseException {
        try{
            return userRepository.findById(userId)
                    .map(user -> new ChallengeRes(user.getNickname(), user.getLevel().getId(), user.getExp(), user.getProfileImgUrl()))
                    .orElse(null);
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
        User user = userRepository.findById(userId).orElse(null);
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
       .map(uc -> new ChallengeListRes(uc.getChallengeId().getId(), uc.getChallengeId().getContent(), uc.isComplete()))
       .collect(Collectors.toList());
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
