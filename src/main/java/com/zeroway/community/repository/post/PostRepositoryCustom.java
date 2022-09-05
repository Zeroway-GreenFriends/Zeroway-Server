package com.zeroway.community.repository.post;

import com.zeroway.community.dto.*;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostListRes> getPostList(Long userId, String sort, Boolean challenge, Boolean review, long page, long size);
    PostRes getPost(Long postId, Long userId);
    List<UserInfo> getPostLikeList(Long postId);
    List<GetPostListByMypageRes> getPostListByUser(Long userId, Long page, Long size);
    List<GetPostListByMypageRes> getPostListByComment(Long userId, Long page, Long size);
    List<GetPostListByMypageRes> getPostListByLike(Long userId, Long page, Long size);
    List<GetPostListByMypageRes> getPostListByScrap(Long userId, Long page, Long size);
}
