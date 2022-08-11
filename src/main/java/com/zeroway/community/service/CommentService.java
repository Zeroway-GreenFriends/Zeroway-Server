package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.CreateCommentReq;
import com.zeroway.community.entity.Comment;
import com.zeroway.community.entity.CommentLike;
import com.zeroway.community.repository.comment.CommentLikeRepository;
import com.zeroway.community.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 저장
    @Transactional
    public void createComment(CreateCommentReq req, Long postId, Long userId) throws BaseException {
        try {
            commentRepository.save(Comment.builder().content(req.getContent()).userId(userId).postId(postId).build());
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
            commentLikeRepository.save(CommentLike.builder().userId(userId).commentId(commentId).build());
            return true; // 좋아요
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) throws BaseException {
        try {
            Comment comment = commentRepository.findByIdAndUserId(commentId, userId)
                    .orElseThrow(() -> new BaseException(REQUEST_ERROR));

            // 이미 삭제된 댓글
            if(comment.getStatus().equals(StatusType.INACTIVE)) throw new BaseException(ALREADY_DELETED);

            // 상태를 INACTIVE 로 수정
            comment.setStatus(StatusType.INACTIVE);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
