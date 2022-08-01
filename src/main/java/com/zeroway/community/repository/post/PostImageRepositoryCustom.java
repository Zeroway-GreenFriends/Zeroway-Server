package com.zeroway.community.repository.post;

import java.util.List;

public interface PostImageRepositoryCustom {

    List<String> findUrlByPostId(Long postId);
}
