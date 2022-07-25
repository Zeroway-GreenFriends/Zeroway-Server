package com.zeroway.tips.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.tips.dto.TermRes;
import com.zeroway.tips.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/term")
@RestController
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @GetMapping()
    public ResponseEntity<?> getTerms(@RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {

        if(page < 1 || size < 1) return ResponseEntity.badRequest().build();

        try {
            List<TermRes> termRes;

            if (keyword == null || keyword.isEmpty()) // 검색어를 입력하지 않은 경우
                termRes = termService.getTerm(page-1, size);
            else termRes = termService.searchTerm(keyword, page - 1, size);

            return ResponseEntity.ok().body(termRes);
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }


    }
}
