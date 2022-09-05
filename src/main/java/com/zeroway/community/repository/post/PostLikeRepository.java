package com.zeroway.community.repository.post;

import com.zeroway.common.StatusType;
import com.zeroway.community.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId);
    List<PostLike> findByUserIdAndStatus(Long userId, StatusType statusType);
}
