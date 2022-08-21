package com.zeroway.store.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.store.dto.StoreListRes;
import com.zeroway.store.service.StoreReviewService;
import com.zeroway.store.service.StoreService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final StoreReviewService storeReviewService;
    private final JwtService jwtService;

    /**
     * 제로웨이스트샵 리스트 API
     * @return 제로웨이스트샵(id, imageUrl, name, item, scoreAvg, addressNew, operatingTime, contact, siteUrl, instagram)
     */
    @GetMapping("/list")
    public ResponseEntity<?> getStoreList(@RequestParam(required = false) String keyword,
                                          @RequestParam int page,
                                          @RequestParam int size) {
        if(page<1 || size<1) return ResponseEntity.badRequest().build();

        try{
            List<StoreListRes> storeListRes;
            if(keyword==null || keyword.isEmpty()) {
                storeListRes = storeService.getStoreList(page-1, size);
            } else storeListRes = storeService.searchStoreList(keyword, page-1, size);

            return ResponseEntity.ok().body(storeListRes);
        } catch(BaseException exception){
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    /**
     * 제로웨이스트샵 상세 조회 API
     * 리뷰는 4개만 조회한다.
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreDetail(@PathVariable Long storeId) {
        try{
            Long userId = jwtService.getUserIdx();
            return ResponseEntity.ok().body(storeService.getStoreDetail(storeId, userId));
        } catch(BaseException exception){
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    /**
     * 제로웨이스트샵 전체 리뷰 조회 API
     */
    @GetMapping("/{storeId}/review")
    public ResponseEntity<?> getAllReviews(@PathVariable Long storeId) {
        try{
            Long userId = jwtService.getUserIdx();
            return ResponseEntity.ok().body(storeReviewService.getAllReview(storeId, userId));
        } catch(BaseException exception){
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }
}
