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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

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

    public ChallengeCompleteRes patchChallengeComplete(Long userId, Long challengeId, Integer exp) throws Exception{
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
            Optional<Level> levelup = levelRepository.findById(user.getLevel().getId() + 1);
            if(levelup.isEmpty()) {
                throw new BaseException(DATABASE_ERROR);
            }
            user.setLevel(levelup.get());
            user.setExp(user.getExp()-100);
        } else if(user.getExp() < 0){
            if(user.getLevel().getId()!=1) {
                Optional<Level> levelDown = levelRepository.findById(user.getLevel().getId() - 1);
                if(levelDown.isEmpty()) {
                    throw new BaseException(DATABASE_ERROR);
                }
                user.setLevel(levelDown.get());
                user.setExp(user.getExp()+100);
            } else {
                user.setExp(0);
            }
        }
        userRepository.save(user);
    }
}