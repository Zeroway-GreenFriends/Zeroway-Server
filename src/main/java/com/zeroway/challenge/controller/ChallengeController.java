package com.zeroway.challenge.controller;

import com.zeroway.challenge.dto.ChallengeRes;
import com.zeroway.challenge.service.ChallengeService;
import com.zeroway.challenge.dto.GetChallengeRes;
import com.zeroway.challenge.dto.PatchChallengeCompleteRes;
import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.user.entity.User;
import com.zeroway.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/challenge")
@Slf4j
public class ChallengeController {

    @Autowired
    private final ChallengeService challengeService;

    @Autowired
    private JwtService jwtService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }


    @ResponseBody
    @GetMapping("")
    public BaseResponse<ChallengeRes> getList() {
        try{
            ChallengeRes challengeRes = challengeService.getList(jwtService.getUserIdx());
            return new BaseResponse<>(challengeRes);
        } catch(BaseException exception){
            log.error(exception.getMessage());
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{challenge_id}/complete")
    public BaseResponse<PatchChallengeCompleteRes> completeChallenge(@PathVariable ("challenge_id") Long challengeId) {
        try{
            challengeService.completeChallenge(jwtService.getUserIdx(),challengeId);
            checkLevelUpgrade(jwtService.getUserIdx());
            PatchChallengeCompleteRes PatchChallengeCompleteRes = challengeService.findUserExp(jwtService.getUserIdx());
            return new BaseResponse<>(PatchChallengeCompleteRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkLevelUpgrade(Long userId) {
        try {
            if(challengeService.checkLevelUpgrade(userId)%4==0){
                challengeService.levelUpgrade(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
