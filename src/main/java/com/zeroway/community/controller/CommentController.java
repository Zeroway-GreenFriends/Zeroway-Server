package com.zeroway.community.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.community.dto.LikeReq;
import com.zeroway.community.dto.ReportReq;
import com.zeroway.community.service.CommentService;
import com.zeroway.cs.entity.report.CategoryOfReport;
import com.zeroway.cs.service.ReportService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.zeroway.common.BaseResponseStatus.UNAUTHORIZED_REQUEST;

@RequestMapping("/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;
    private final ReportService reportService;

    /**
     * 댓글 좋아요 및 좋아요 취소 API
     * @param commentId 댓글 id
     */
    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> like(@PathVariable Long commentId, @RequestBody LikeReq req) {
        try {
            Long userId = jwtService.getUserIdx();
            commentService.like(userId, commentId, req.isLike());
            return ResponseEntity.ok().build();
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

    /**
     * 댓글 삭제 API
     * @param commentId 댓글 id
     */
    @PatchMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        try {
            Long userId = jwtService.getUserIdx();
            commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok().build();
        } catch (BaseException e) {
            if(e.getStatus().equals(UNAUTHORIZED_REQUEST))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(e.getStatus()));
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

    /**
     * 댓글 신고 API
     * @param reportReq (targetId-신고할 댓글 id, type-신고유형)
     */
    @PostMapping("/report")
    public ResponseEntity<?> reportComment(@RequestBody ReportReq reportReq) {
        try{
            Long userId = jwtService.getUserIdx();
            CategoryOfReport category = CategoryOfReport.COMMENT;
            reportService.reportTarget(userId, category, reportReq);
            return ResponseEntity.ok().build();
        } catch (BaseException exception) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }
}
