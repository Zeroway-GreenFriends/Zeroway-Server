package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeListRes;
import com.zeroway.challenge.dto.GetChallengeRes;
import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


//    @GetMapping("/list")
//    public List<Challenge> challengeList(@RequestParam Long id) {
//        return challengeService.findList();
//    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetChallengeRes>> getList() {
        try{
            List<GetChallengeRes> GetChallengeRes = challengeService.getList(jwtService.getUserIdx());
            return new BaseResponse<>(GetChallengeRes);
        } catch(BaseException exception){
            log.error(exception.getMessage());
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{challenge_id}/complete")
    public BaseResponse<String> completeChallenge( @PathVariable ("challenge_id") Long challengeId) {
        try{
            challengeService.completeChallenge(jwtService.getUserIdx(),challengeId);
            String result = "챌린지 수행 완료";
            checkLevelUpgrade(jwtService.getUserIdx());
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkLevelUpgrade(Long userId) {
        try {
            if(challengeService.checkLevelUpgrade(userId)>=5){
                challengeService.levelUpgrade(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
