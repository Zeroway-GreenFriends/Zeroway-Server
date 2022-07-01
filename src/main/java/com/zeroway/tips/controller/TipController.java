package com.zeroway.tips.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.tips.dto.AllTipRes;
import com.zeroway.tips.service.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tip")
@RequiredArgsConstructor
public class TipController {

    private final TipService tipService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllTips() {

        try {
            AllTipRes allTips = tipService.getAllTips();
            System.out.println("new BaseResponse<>(allTips) = " + new BaseResponse<>(allTips));
            return ResponseEntity.ok().body(new BaseResponse<>(allTips));
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }



}
