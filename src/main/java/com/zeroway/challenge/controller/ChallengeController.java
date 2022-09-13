package com.zeroway.challenge.controller;

import com.zeroway.challenge.dto.ChallengeCompleteRes;
import com.zeroway.challenge.dto.ChallengeListRes;
import com.zeroway.challenge.dto.ChallengeRes;
import com.zeroway.challenge.service.ChallengeService;
import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
@Slf4j
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;
    private final JwtService jwtService;

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
     * @return 챌린지(id, content, complete) 랜덤 5개
     */
    @ResponseBody
    @GetMapping("list/today")
    public ResponseEntity<?> getTodayChallengeList() {
        try{
            List<ChallengeListRes> challengeListRes = challengeService.getChallenge(jwtService.getUserIdx(), 5);
            return ResponseEntity.ok().body(challengeListRes);
        } catch(BaseException exception){
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    /**
     * 챌린지 수행(수행취소) API
     * @return 유저(level)
     */
    @ResponseBody
    @PatchMapping("/{challenge_id}/complete")
    public ResponseEntity<?> patchChallengeComplete(@PathVariable ("challenge_id") Long challengeId) {
        try{
            ChallengeCompleteRes ChallengeCompleteRes = challengeService.patchChallengeComplete(jwtService.getUserIdx(), challengeId, 20);
            return ResponseEntity.ok().body(ChallengeCompleteRes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage()));
        }
    }


    /**
     * 레벨별 챌린지 API
     * @return 챌린지(id, content, complete) (유저 레벨별)
     */
    /*
    @ResponseBody
    @GetMapping("list")
    public ResponseEntity<?> getChallengeList() {
        try{
            List<ChallengeListRes> challengeListRes = challengeService.getChallengeList(jwtService.getUserIdx());
            return ResponseEntity.ok().body(challengeListRes);
        } catch(BaseException exception){
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }
    */
}
