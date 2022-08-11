package com.zeroway.cs.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.cs.dto.AnnounceListRes;
import com.zeroway.cs.service.AnnounceService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/announce")
@RestController
@RequiredArgsConstructor
public class AnnounceController {

    private final AnnounceService announceService;

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

}
