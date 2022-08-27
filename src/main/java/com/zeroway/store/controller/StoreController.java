package com.zeroway.store.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.store.dto.StoreListRes;
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
    private final JwtService jwtService;

    /**
     * 제로웨이스트샵 리스트 API
     * @return 제로웨이스트샵(id, imageUrl, name, item, addressNew, operatingTime, contact, siteUrl, instagram, description)
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
     * @param storeId
     * @return 이미지 url, 이름, 품목, 주소, 운영시간, 전화번호, 사이트 url, 인스타그램, 설명(제로웨이스트 실천 내용 포함)
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreDetail(@PathVariable Long storeId) {
        try{
            return ResponseEntity.ok().body(storeService.getStoreDetail(storeId));
        } catch(BaseException exception){
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }
}