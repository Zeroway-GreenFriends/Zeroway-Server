package com.zeroway.community.repository;

import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.dto.PostRes;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostListRes> getPostList(Long userId, String sort);
    PostRes getPost(Long postId, Long userId);
}
