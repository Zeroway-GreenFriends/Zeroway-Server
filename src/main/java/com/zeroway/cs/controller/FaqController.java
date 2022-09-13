package com.zeroway.cs.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.cs.dto.FaqListRes;
import com.zeroway.cs.dto.FaqRes;
import com.zeroway.cs.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/faq")
@RestController
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;


    /**
     * 자주 묻는 질문 전체 조회 API
     * @return FAQ(id, 질문)
     */
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

    /**
     * 자주 묻는 질문 상세 조회 API
     * @param faqId
     * @return FAQ(질문, 답변)
     */
    @ResponseBody
    @GetMapping("/{faq_id}")
    public ResponseEntity<?> getFaq(@PathVariable("faq_id") Long faqId) {
        try{
            FaqRes faqList = faqService.getFaq(faqId);
            return ResponseEntity.ok().body(faqList);

        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

}
