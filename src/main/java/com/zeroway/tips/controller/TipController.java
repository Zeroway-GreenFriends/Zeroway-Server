package com.zeroway.tips.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.tips.dto.AllTipRes;
import com.zeroway.tips.dto.TodayTipsRes;
import com.zeroway.tips.service.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tip")
@RequiredArgsConstructor
public class TipController {

    private final TipService tipService;

    /**
     * 오늘의 실천 tip 조회 api
     * @return 랜덤 3개 팁
     */
    @GetMapping()
    public ResponseEntity<?> getTodayTips() throws BaseException {
        List<TodayTipsRes> randomTips = tipService.getRandomTips(3);
        return ResponseEntity.ok().body(randomTips);
    }

}
