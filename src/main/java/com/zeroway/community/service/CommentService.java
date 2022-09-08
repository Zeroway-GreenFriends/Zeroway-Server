package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.CreateCommentReq;
import com.zeroway.community.entity.Comment;
import com.zeroway.community.entity.CommentLike;
import com.zeroway.community.repository.comment.CommentLikeRepository;
import com.zeroway.community.repository.comment.CommentRepository;
import com.zeroway.cs.repository.ReportRepository;
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
     */
    @Transactional
    public void like(Long userId, Long commentId, boolean like) throws BaseException {
        try {
            Optional<CommentLike> optional = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
            if (optional.isPresent()) {
                CommentLike commentLike = optional.get();
                if(like) { // 좋아요
                    commentLike.setStatus(StatusType.ACTIVE);
                } else { // 좋아요 취소
                    commentLike.setStatus(StatusType.INACTIVE);
                }
            }
            // CommentLike 없는 경우
            // 좋아요만 수행 (좋아요 취소는 수행하지 않음)
            else if (like) commentLikeRepository.save(CommentLike.builder().userId(userId).commentId(commentId).build());
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
