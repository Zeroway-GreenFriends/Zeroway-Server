package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeRes;
import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @Autowired
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

//    @GetMapping("/list")
//    public List<Challenge> challengeList(@RequestParam Long id) {
//        return challengeService.findList();
//    }

    @ResponseBody
    @GetMapping("/{user_id}")
    public BaseResponse<List<GetChallengeRes>> getPosts(@PathVariable("user_id") int userId) {
        try{
            List<GetChallengeRes> GetChallengeRes = challengeService.getList(userId);
            return new BaseResponse<>(GetChallengeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
