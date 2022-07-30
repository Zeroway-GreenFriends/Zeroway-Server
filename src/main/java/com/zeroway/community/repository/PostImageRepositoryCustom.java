package com.zeroway.community.repository;

import java.util.List;

public interface PostImageRepositoryCustom {

    List<String> findUrlByPostId(Long postId);
}
