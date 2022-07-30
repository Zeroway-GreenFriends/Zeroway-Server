package com.zeroway.community.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.dto.PostRes;
import com.zeroway.community.service.PostService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zeroway.common.BaseResponseStatus.INVALID_PARAMETER_VALUE;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;
    private final List<String> sortColumns = new ArrayList<>(Arrays.asList("createdAt", "like"));

    /**
     * 커뮤니티 글 전체 목록 조회 API
     */
    @GetMapping("/list")
    public ResponseEntity<?> getPostList(@RequestParam(defaultValue = "createdAt") String sort) {
        try {
            if (sortColumns.contains(sort)) {
                Long userId = jwtService.getUserIdx();
                List<PostListRes> postList = postService.getPostList(userId, sort);
                return ResponseEntity.ok().body(postList);
            }
            throw new BaseException(INVALID_PARAMETER_VALUE);
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable Long postId) {
        try{
            PostRes postRes = postService.getPost(postId);
            return ResponseEntity.ok().body(postRes);
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }
}
