package com.zeroway.cs.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.cs.dto.AnnounceListRes;
import com.zeroway.cs.dto.AnnounceRes;
import com.zeroway.cs.service.AnnounceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/announce")
@RestController
@RequiredArgsConstructor
public class AnnounceController {

    private final AnnounceService announceService;

    /**
     * 공지사항 전체 조회 API
     * @return 공지사항(Id, 제목, 생성시간), 최신순 정렬
     */
    @ResponseBody
    @GetMapping("")
    public ResponseEntity<?> getAnnounceList() {
        try{
            List<AnnounceListRes> announceListRes = announceService.getAnnounceList();
            return ResponseEntity.ok().body(announceListRes);
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

    /**
     * 공지사항 상세 조회 API
     * @param announceId (공지사항 Id)
     * @return 공지사항(제목, 내용, 생성시간)
     */
    @ResponseBody
    @GetMapping("/{announce_id}")
    public ResponseEntity<?> getAnnounce(@PathVariable("announce_id") Long announceId) {
        try{
            AnnounceRes announceRes = announceService.getAnnounce(announceId);
            return ResponseEntity.ok().body(announceRes);
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

}
