package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.community.dto.CreateCommentReq;
import com.zeroway.community.entity.Comment;
import com.zeroway.community.entity.Post;
import com.zeroway.community.repository.CommentRepository;
import com.zeroway.community.repository.PostRepository;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 저장
    @Transactional
    public void createComment(CreateCommentReq req, Long postId, Long userId) throws BaseException {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_JWT));
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(INVALID_POST_ID));
        try {
            commentRepository.save(Comment.builder().content(req.getContent()).user(user).post(post).build());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
