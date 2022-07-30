package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.community.repository.CommentRepository;
import com.zeroway.community.repository.PostImageRepository;
import com.zeroway.community.repository.PostRepository;
import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.dto.PostRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.INVALID_POST_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;


    // 전체 글 조회
    public List<PostListRes> getPostList(Long userId, String sort) throws BaseException {
        List<PostListRes> result = new ArrayList<>();
        try {
            for (PostListRes post : postRepository.getPostList(userId, sort)) {
                Long postId = post.getPostId();
                // 게시글 이미지 조회
                post.getImageList().addAll(postImageRepository.findUrlByPostId(postId));
                result.add(post);
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 글 상세 조회
    public PostRes getPost(Long postId, Long userId) throws BaseException {
        PostRes post;
        try {
            post = postRepository.getPost(postId, userId);

            //유효하지 않은 게시글 id
            if(post == null)
                throw new BaseException(INVALID_POST_ID);

            // 게시글 이미지 조회
            post.getImageList().addAll(postImageRepository.findUrlByPostId(postId));

            // 댓글 조회
            post.getCommentList().addAll(commentRepository.getCommentList(postId, userId));

            return post;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
