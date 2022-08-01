package com.zeroway.community.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.community.dto.CreateCommentReq;
import com.zeroway.community.dto.CreatePostReq;
import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.dto.PostRes;
import com.zeroway.community.service.CommentService;
import com.zeroway.community.service.PostService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final CommentService commentService;
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

    /**
     * 게시글 상세 조회 API
     * @param postId 게시글 id
     */
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable Long postId) {
        try{
            Long userId = jwtService.getUserIdx();
            PostRes postRes = postService.getPost(postId, userId);
            return ResponseEntity.ok().body(postRes);
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

    /**
     * 글 작성 api
     * @param post - 내용, 챌린지 인증 여부
     * @param images - 이미지 파일 리스트
     */
    @PostMapping()
    public ResponseEntity<?> createPost(@RequestPart CreatePostReq post,
                                        @RequestPart List<MultipartFile> images) {
        try {
            Long userId = jwtService.getUserIdx();
            postService.createPost(post, images, userId);
            return ResponseEntity.ok().build();
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }


    /**
     * 댓글 작성 api
     * @param postId 게시글 id
     * @param req 댓글 내용
     */
    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long postId,
                                           @RequestBody CreateCommentReq req) {
        try {
            Long userId = jwtService.getUserIdx();
            commentService.createComment(req, postId, userId);
            return ResponseEntity.ok().build();
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }
}
