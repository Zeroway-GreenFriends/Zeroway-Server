package com.zeroway.community.repository.comment;

import com.zeroway.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    List<Comment> findByPostId(Long post);
}
