package com.zeroway.community.repository;

import com.zeroway.community.dto.PostListRes;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostListRes> getPostList(Long userId, String sort);
}
