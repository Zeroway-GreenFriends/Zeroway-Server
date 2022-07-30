package com.zeroway.community.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.dto.QPostListRes;
import com.zeroway.community.entity.QBookmark;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static com.zeroway.community.entity.QBookmark.bookmark;
import static com.zeroway.community.entity.QComment.comment;
import static com.zeroway.community.entity.QPost.post;
import static com.zeroway.community.entity.QPostLike.postLike;
import static com.zeroway.user.entity.QUser.user;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PostListRes> getPostList(Long userId, String sort) {
        if(sort.equals("createdAt")) {  // 최신순 조회
            return getPostListResJPAQuery(userId)
                    .orderBy(post.createdAt.desc())
                    .fetch();
        } else {  // 인기순 조회
            // alias 설정
            NumberPath<Long> postLikeCount = Expressions.numberPath(Long.class, "postLikeCount");
            return getPostListResJPAQuery(userId)
                    .orderBy(postLikeCount.desc())
                    .fetch();
        }

    }

    // 게시글 조회 jpa query - 정렬 적용 전
    private JPAQuery<PostListRes> getPostListResJPAQuery(Long userId) {
        return queryFactory
                .select(new QPostListRes(
                        post.id, post.title, post.content, post.createdAt,
                        user.nickname, user.profileImgUrl,
                        ExpressionUtils.as(
                                // 좋아요 개수 서브 쿼리
                                select(postLike.count().intValue()).from(postLike).where(postLike.post.eq(post), postLike.status.eq(StatusType.ACTIVE)),
                                "postLikeCount"
                        ),
                        select(comment.count().intValue()).from(comment).where(comment.post.eq(post), comment.status.eq(StatusType.ACTIVE)), // 댓글 개수 서브 쿼리
                        select(postLike.isNotNull()).from(postLike).where(postLike.post.eq(post), postLike.user.id.eq(userId), postLike.status.eq(StatusType.ACTIVE)), // 좋아요 여부 서브 쿼리
                        select(bookmark.isNotNull()).from(bookmark).where(bookmark.post.eq(post), bookmark.user.id.eq(userId), bookmark.status.eq(StatusType.ACTIVE))  // 북마크 여부 서브 쿼리
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(post.status.eq(StatusType.ACTIVE));
    }
}
