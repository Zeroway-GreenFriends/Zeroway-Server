package com.zeroway.challenge.controller;

import com.zeroway.challenge.dto.ChallengeListRes;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/challenge")
@Slf4j
public class ChallengeController {

    private final ChallengeService challengeService;

    @Autowired
    private JwtService jwtService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    /**
     * 챌린지 프로필 API
     * @return 유저(nickname, level, exp, imgUrl)
     */
    @ResponseBody
    @GetMapping("")
    public ResponseEntity<?> getList() {
        try{
            ChallengeRes challengeRes = challengeService.getList(jwtService.getUserIdx());
            return ResponseEntity.ok().body(challengeRes);
        } catch(BaseException exception){
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    /**
     * 오늘의 챌린지 API
     * @return 챌린지(id, content, complete) 랜덤 5개 (유저 레벨별 + 수행 안 한 것)
     */
    @ResponseBody
    @GetMapping("list")
    public ResponseEntity<?> getChallengeList() {
        try{
            List<ChallengeListRes> challengeListRes = challengeService.getChallengeList(jwtService.getUserIdx(), 5);
            return ResponseEntity.ok().body(challengeListRes);
        } catch(BaseException exception){
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{challenge_id}/complete")
    public BaseResponse<PatchChallengeCompleteRes> completeChallenge(@PathVariable ("challenge_id") Long challengeId) {
        try{
            challengeService.completeChallenge(jwtService.getUserIdx(),challengeId);
            PatchChallengeCompleteRes PatchChallengeCompleteRes = challengeService.findUserExp(jwtService.getUserIdx());
            return new BaseResponse<>(PatchChallengeCompleteRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
