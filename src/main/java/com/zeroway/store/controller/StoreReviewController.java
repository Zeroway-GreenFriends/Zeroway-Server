package com.zeroway.store.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.store.dto.CreateReviewReq;
import com.zeroway.store.service.StoreReviewService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/review")
@RestController
@RequiredArgsConstructor
public class StoreReviewController {

    private final StoreReviewService storeReviewService;
    private final JwtService jwtService;


    /**
     * 제로웨이스트 샵 리뷰 작성 API
     * @param req - storeId, content, score
     */
    @PostMapping()
    public ResponseEntity<?> createReview(@RequestBody CreateReviewReq req) {
        try {
            Long userId = jwtService.getUserIdx();
            storeReviewService.createReview(req, userId);
            return ResponseEntity.ok().build();
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }
}
