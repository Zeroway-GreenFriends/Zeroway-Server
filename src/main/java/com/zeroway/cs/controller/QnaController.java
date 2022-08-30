package com.zeroway.cs.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.cs.dto.QnaListRes;
import com.zeroway.cs.dto.QnaReq;
import com.zeroway.cs.dto.QnaRes;
import com.zeroway.cs.service.QnaService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 문의 내역 상세 조회 API
     * @param qnaId
     * @return 문의 유형, 문의 내용, 문의 사진 url(리스트), 답변
     */
    @ResponseBody
    @GetMapping("/{qna_id}")
    public ResponseEntity<?> getQna(@PathVariable("qna_id") Long qnaId) {
        try{
            QnaRes qnaRes = qnaService.getQna(qnaId);
            return ResponseEntity.ok().body(qnaRes);
        } catch (BaseException exception) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

//    /**
//     * *
//     * 문의 내역 작성 API
//     * @param qnaReq Qna(문의 유형, 질문)
//     * @param imgs 이미지
//     */
//    @PostMapping(consumes={"multipart/form-data"})
//    public ResponseEntity<?> createQna(@RequestPart(value="qnaReq") QnaReq qnaReq, @RequestPart(value="imgs", required = false)List<MultipartFile> imgs) {
//        try{
//            Long userId = jwtService.getUserIdx();
//            qnaService.createQna(qnaReq, userId, imgs);
//            return ResponseEntity.ok().build();
//        } catch (BaseException exception) {
//            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
//        }

    /**
     * 문의 내역 작성 API
     * @param qnaReq Qna(문의 유형, 질문)
     */
        @PostMapping(consumes={"multipart/form-data"})
        public ResponseEntity<?> createQna(@RequestPart(value="qnaReq") QnaReq qnaReq) {
            try{
                Long userId = jwtService.getUserIdx();
                qnaService.createQna(qnaReq, userId);
                return ResponseEntity.ok().build();
            } catch (BaseException exception) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
            }
    }
}
