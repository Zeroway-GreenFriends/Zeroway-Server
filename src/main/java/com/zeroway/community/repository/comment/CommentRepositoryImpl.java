package com.zeroway.community.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.CommentListRes;
import com.zeroway.community.dto.QCommentListRes;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static com.zeroway.community.entity.QComment.comment;
import static com.zeroway.community.entity.QCommentLike.commentLike;
import static com.zeroway.community.repository.post.PostRepositoryImpl.calculateWeeksAgo;
import static com.zeroway.user.entity.QUser.user;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 댓글 목록 조회
     * @param postId 게시글 id
     * @param userId 회원 id (좋아요 여부)
     */
    @Override
    public List<CommentListRes> getCommentList(Long postId, Long userId) {
        return queryFactory
                .select(
                        new QCommentListRes(
                                user.id, user.nickname, user.profileImgUrl,
                                comment.id ,comment.content,
                                calculateWeeksAgo(comment.createdAt),
                                select(commentLike.count().intValue()).from(commentLike).where(commentLike.commentId.eq(comment.id), commentLike.status.eq(StatusType.ACTIVE)),
                                select(commentLike.isNotNull()).from(commentLike).where(commentLike.commentId.eq(comment.id), commentLike.status.eq(StatusType.ACTIVE), commentLike.userId.eq(userId))
                        )
                )
                .from(comment)
                .join(user).on(comment.userId.eq(user.id))
                .where(
                        comment.postId.eq(postId),
                        comment.status.eq(StatusType.ACTIVE)
                ).fetch();
    }
}
