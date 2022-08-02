package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.CreateCommentReq;
import com.zeroway.community.entity.Comment;
import com.zeroway.community.entity.CommentLike;
import com.zeroway.community.entity.Post;
import com.zeroway.community.entity.PostLike;
import com.zeroway.community.repository.comment.CommentLikeRepository;
import com.zeroway.community.repository.comment.CommentRepository;
import com.zeroway.community.repository.post.PostRepository;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

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

    /**
     * 좋아요 기능
     * @return true 좋아요, false 좋아요 취소
     */
    @Transactional
    public boolean like(Long userId, Long commentId) throws BaseException {
        try {
            Optional<CommentLike> optional = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
            if (optional.isPresent()) {
                CommentLike like = optional.get();
                if(like.getStatus().equals(StatusType.ACTIVE)) {// 좋아요 취소
                    optional.get().setStatus(StatusType.INACTIVE);
                    return false;
                } else {
                    optional.get().setStatus(StatusType.ACTIVE); // 좋아요
                    return true;
                }
            }
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_JWT));
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BaseException(REQUEST_ERROR));
            commentLikeRepository.save(CommentLike.builder().user(user).comment(comment).build());
            return true; // 좋아요
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
