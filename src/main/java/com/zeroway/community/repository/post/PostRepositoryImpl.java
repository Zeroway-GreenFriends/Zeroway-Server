package com.zeroway.community.repository.post;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.*;

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

    /**
     * 게시글 전체 목록 조회
     * @param userId 회원 id (좋아요 여부, 북마크 여부)
     * @param sort 정렬 기준
     */
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

    /**
     * 게시글 상세 조회
     * @param userId 회원 id (좋아요 여부, 북마크 여부)
     * @param postId 게시글 id
     */
    @Override
    public PostRes getPost(Long postId, Long userId) {
        return queryFactory
                .select(
                    new QPostRes(
                            post.id, user.nickname, user.profileImgUrl, post.content, post.createdAt, post.challenge,
                            postLikeCount(),
                            commentCount(),
                            postLiked(userId),
                            bookmarked(userId))
                )
                .from(post)
                .join(user).on(post.userId.eq(user.id))
                .where(post.id.eq(postId), post.status.eq(StatusType.ACTIVE))
                .fetchOne();
    }

    /**
     * 좋아요 목록 조회
     * @param postId 게시글 id
     */
    @Override
    public List<UserInfo> getPostLikeList(Long postId) {
        return queryFactory
                .select(new QUserInfo(user.id, user.nickname, user.profileImgUrl, user.level.id))
                .from(postLike)
                .join(user).on(postLike.userId.eq(user.id))
                .where(
                        postLike.postId.eq(postId),
                        postLike.status.eq(StatusType.ACTIVE)
                )
                .fetch();
    }


    // 게시글 조회 jpa query - 정렬 적용 전
    private JPAQuery<PostListRes> getPostListResJPAQuery(Long userId) {
        return queryFactory
                .select(new QPostListRes(
                        post.id, post.content, post.createdAt, post.challenge,
                        user.nickname, user.profileImgUrl,
                        ExpressionUtils.as( // 좋아요 개수 서브 쿼리
                                postLikeCount(), "postLikeCount"
                        ),
                        commentCount(), // 댓글 개수 서브 쿼리
                        postLiked(userId), // 좋아요 여부 서브 쿼리
                        bookmarked(userId)  // 북마크 여부 서브 쿼리
                ))
                .from(post)
                .leftJoin(user).on(post.userId.eq(user.id))
                .where(post.status.eq(StatusType.ACTIVE));
    }

    // 좋아요 개수 쿼리
    private JPQLQuery<Integer> postLikeCount() {
        return select(postLike.count().intValue()).from(postLike).where(postLike.postId.eq(post.id), postLike.status.eq(StatusType.ACTIVE));
    }

    // 댓글 개수 쿼리
    private JPQLQuery<Integer> commentCount() {
        return select(comment.count().intValue()).from(comment).where(comment.postId.eq(post.id), comment.status.eq(StatusType.ACTIVE));
    }

    // 게시글 좋아요 여부 쿼리
    private JPQLQuery<Boolean> postLiked(Long userId) {
        return select(postLike.isNotNull()).from(postLike).where(postLike.postId.eq(post.id), postLike.userId.eq(userId), postLike.status.eq(StatusType.ACTIVE));
    }

    // 북마크 여부 쿼리
    private JPQLQuery<Boolean> bookmarked(Long userId) {
        return select(bookmark.isNotNull()).from(bookmark).where(bookmark.postId.eq(post.id), bookmark.userId.eq(userId), bookmark.status.eq(StatusType.ACTIVE));
    }
}
