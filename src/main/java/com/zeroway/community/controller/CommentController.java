package com.zeroway.community.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.community.dto.CreateLikeRes;
import com.zeroway.community.service.CommentService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    /**
     * 댓글 좋아요 및 좋아요 취소 API
     * @param commentId 댓글 id
     */
    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> like(@PathVariable Long commentId) {
        try {
            Long userId = jwtService.getUserIdx();
            boolean like = commentService.like(userId, commentId);
            return ResponseEntity.ok().body(new CreateLikeRes(like));
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }


    }
}
