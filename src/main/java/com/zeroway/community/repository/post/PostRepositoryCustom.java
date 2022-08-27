package com.zeroway.community.repository.post;

import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.dto.PostRes;
import com.zeroway.community.dto.UserInfo;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostListRes> getPostList(Long userId, String sort, Boolean challenge, Boolean review, int page, int size);
    PostRes getPost(Long postId, Long userId);
    List<UserInfo> getPostLikeList(Long postId);
}
