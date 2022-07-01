package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.community.entity.Comment;
import com.zeroway.community.repository.CommentRepository;
import com.zeroway.community.repository.PostRepository;
import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.dto.PostRes;
import com.zeroway.community.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
    private final CommentRepository commentRepository;

    // 전체 글 조회
    public List<PostListRes> getPostList() throws BaseException {
        List<PostListRes> result = new ArrayList<>();
        try {
            // 최신순 조회
            for (Post post : postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))) {
                result.add(PostListRes.builder().postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .username(post.getUser().getNickname())
                        .build());
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 글 상세 조회
    public PostRes getPost(Long postId) throws BaseException {
        Post post;
        try {
            post = postRepository.findById(postId).orElse(null);
            //유효하지 않은 게시글 id
            if(post == null)
                throw new BaseException(INVALID_POST_ID);
            List<Comment> commentList = commentRepository.findByPostId(postId);
            return new PostRes(post, commentList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
