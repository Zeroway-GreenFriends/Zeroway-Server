package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponseStatus;
import com.zeroway.community.PostRepository;
import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public List<PostListRes> getPostList() throws BaseException {
        List<PostListRes> result = new ArrayList<>();
        try {
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
}
