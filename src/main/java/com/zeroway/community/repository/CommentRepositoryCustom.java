package com.zeroway.community.repository;

import com.zeroway.community.dto.CommentListRes;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentListRes> getCommentList(Long postId, Long userId);
}