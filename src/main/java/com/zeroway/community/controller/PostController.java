package com.zeroway.community.controller;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import com.zeroway.community.dto.*;
import com.zeroway.community.service.CommentService;
import com.zeroway.community.service.PostLikeService;
import com.zeroway.community.service.PostService;
import com.zeroway.cs.entity.report.CategoryOfReport;
import com.zeroway.cs.service.ReportService;
import com.zeroway.utils.JwtService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zeroway.common.BaseResponseStatus.INVALID_PARAMETER_VALUE;
import static com.zeroway.common.BaseResponseStatus.UNAUTHORIZED_REQUEST;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;
    private final CommentService commentService;
    private final PostLikeService likeService;
    private final ReportService reportService;
    private final List<String> sortColumns = new ArrayList<>(Arrays.asList("createdAt", "like"));

    /**
     * 커뮤니티 글 전체 목록 조회 API
     */
    @GetMapping("/list")
    public ResponseEntity<?> getPostList(@RequestParam(defaultValue = "createdAt") String sort,
                                         @RequestParam(required = false) Boolean challenge,
                                         @RequestParam(required = false) Boolean review,
                                         @RequestParam(defaultValue = "1") long page,
                                         @RequestParam(defaultValue = "5") long size) throws Exception {
            if (sortColumns.contains(sort)) {
                Long userId = jwtService.getUserIdx();
                List<PostListRes> postList = postService.getPostList(userId, sort, challenge, review, page, size);
                JSONObject res = new JSONObject();
                res.put("data", postList);
                return ResponseEntity.ok().body(res);
            }
            throw new BaseException(INVALID_PARAMETER_VALUE);
    }

    /**
     * 게시글 상세 조회 API
     * @param postId 게시글 id
     */
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable Long postId) throws Exception {
        Long userId = jwtService.getUserIdx();
        PostRes postRes = postService.getPost(postId, userId);
        return ResponseEntity.ok().body(postRes);
    }

    /**
     * 글 작성 API
     * @param post   - 내용, 챌린지 인증 여부
     * @param images - 이미지 파일 리스트
     */
    @PostMapping()
    public ResponseEntity<?> createPost(@RequestPart CreatePostReq post,
                                        @RequestPart(required = false) List<MultipartFile> images) throws Exception{
            Long userId = jwtService.getUserIdx();
            postService.createPost(post, images, userId);
            return ResponseEntity.ok().build();
    }

    /**
     * 댓글 작성 API
     * @param postId 게시글 id
     * @param req    댓글 내용
     */
    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long postId,
                                           @RequestBody CreateCommentReq req) throws Exception {
        Long userId = jwtService.getUserIdx();
        commentService.createComment(req, postId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 좋아요 및 좋아요 취소 API
     * @param postId 게시글 id
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> like(@PathVariable Long postId, @RequestBody LikeReq req) throws Exception {
        Long userId = jwtService.getUserIdx();
        likeService.like(userId, postId, req.isLike());
        return ResponseEntity.ok().build();
    }

    /**
     * 좋아요 목록 조회 API
     * @param postId 게시글 id
     */
    @GetMapping("/{postId}/like")
    public ResponseEntity<?> getLikeList(@PathVariable Long postId) throws Exception {
        LikeListRes res = likeService.getLikeList(postId);
        return ResponseEntity.ok().body(res);
    }

    /**
     * 북마크 및 북마크 취소 API
     * @param postId 게시글 id
     */
    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<?> bookmark(@PathVariable Long postId, @RequestBody BookmarkReq req) throws Exception {
        Long userId = jwtService.getUserIdx();
        postService.bookmark(userId, postId, req.isBookmark());
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제 API
     * @param postId 게시글 id
     */
    @PatchMapping("/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) throws Exception {
        Long userId = jwtService.getUserIdx();
        postService.deletePost(postId, userId);
        return ResponseEntity.ok().build();

    }

    /**
     * 글 신고 API
     * @param reportReq (targetId-신고할 글 id, type-신고유형)
     */
    @PostMapping("/report")
    public ResponseEntity<?> reportPost(@RequestBody ReportReq reportReq) {
        try{
            Long userId = jwtService.getUserIdx();
            CategoryOfReport category = CategoryOfReport.POST;
            reportService.reportTarget(userId, category, reportReq);
            return ResponseEntity.ok().build();
        } catch (BaseException exception) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    /**
     * 내가 쓴 글 조회 API
     */
    @GetMapping("/user")
    public ResponseEntity<BaseResponse<List<GetPostListByMypageRes>>> getPostListByUser(@RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "30") Long size) {
        try {
            List<GetPostListByMypageRes> postListByUser = postService.getPostListByUser(page, size);
            return ResponseEntity.ok().body(new BaseResponse<>(postListByUser));
        } catch (BaseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

    /**
     * 내가 댓글 단 글 조회 API
     */
    @GetMapping("/comment")
    public ResponseEntity<BaseResponse<List<GetPostListByMypageRes>>> getPostListByComment(@RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "30") Long size) {
        try {
            List<GetPostListByMypageRes> postListByComment = postService.getPostListBycomment(page, size);
            return ResponseEntity.ok().body(new BaseResponse<>(postListByComment));
        } catch (BaseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }

    /**
     * 좋아요 누른 글 조회 API
     */
    @GetMapping("/like")
    public ResponseEntity<BaseResponse<List<GetPostListByMypageRes>>> getPostListByLike(@RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "30") Long size) {
        try {
            List<GetPostListByMypageRes> resultList = postService.getPostListByLike(page, size);
            return ResponseEntity.ok().body(new BaseResponse<>(resultList));
        } catch (BaseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }

    }

    /**
     * 스크랩한 글 조회 API
     */
    @GetMapping("/scrap")
    public ResponseEntity<BaseResponse<List<GetPostListByMypageRes>>> getPostListByScrap(@RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "30") Long size) {
        try {
            List<GetPostListByMypageRes> resultList = postService.getPostListByScrap(page, size);
            return ResponseEntity.ok().body(new BaseResponse<>(resultList));
        } catch (BaseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }

    }

}


