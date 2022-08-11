package com.zeroway.community.repository.comment;

import com.zeroway.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CommentRepository
        extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    List<Comment> findByPostId(Long post);
    Optional<Comment> findByIdAndUserId(Long id, Long userId);
}
