package com.zeroway.cs.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.cs.dto.QnaListRes;
import com.zeroway.cs.service.QnaService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/qna")
@RestController
@RequiredArgsConstructor
public class QnaController {

    private final JwtService jwtService;
    private final QnaService qnaService;


    /**
     * 문의 내역 조회 API
     * @return qna(Id, 문의 유형)
     */
    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<?> getQnaList() {
        try{
            Long userId = jwtService.getUserIdx();
            List<QnaListRes> qnaListRes = qnaService.getQnaList(userId);
            return ResponseEntity.ok().body(qnaListRes);
        } catch (BaseException exception) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }
}
