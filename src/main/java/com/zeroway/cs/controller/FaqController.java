package com.zeroway.cs.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.cs.dto.AnnounceListRes;
import com.zeroway.cs.dto.FaqListRes;
import com.zeroway.cs.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/faq")
@RestController
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<?> getFaqList() {
        try{
            List<FaqListRes> faqList = faqService.getFaqList();
            return ResponseEntity.ok().body(faqList);

        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

}
