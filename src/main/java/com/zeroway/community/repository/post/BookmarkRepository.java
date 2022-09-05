package com.zeroway.community.repository.post;

import com.zeroway.common.StatusType;
import com.zeroway.community.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserIdAndPostId(Long userId, Long postId);
    List<Bookmark> findByUserIdAndStatus(Long userId, StatusType statusType);
}
