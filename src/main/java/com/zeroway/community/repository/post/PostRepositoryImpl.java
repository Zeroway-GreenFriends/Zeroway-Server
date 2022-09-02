package com.zeroway.community.repository.post;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.*;
import static com.querydsl.jpa.JPAExpressions.*;
import static com.zeroway.community.entity.QBookmark.bookmark;
import static com.zeroway.community.entity.QComment.comment;
import static com.zeroway.community.entity.QPost.post;
import static com.zeroway.community.entity.QPostImage.postImage;
import static com.zeroway.community.entity.QPostLike.postLike;
import static com.zeroway.user.entity.QUser.user;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 게시글 전체 목록 조회
     *
     * @param userId    회원 id (좋아요 여부, 북마크 여부)
     * @param sort      정렬 기준
     * @param challenge 챌린지 인증 여부
     * @param review    리뷰 여부
     * @param page      페이지
     * @param size      개수
     */
    @Override
    public List<PostListRes> getPostList(Long userId, String sort, Boolean challenge, Boolean review, long page, long size) {
        if(sort.equals("createdAt")) {  // 최신순 조회
            return getPostListResJPAQuery(userId, challenge, review)
                    .orderBy(post.createdAt.desc())
                    .offset((page - 1) * size)
                    .limit(size)
                    .fetch();
        } else {  // 인기순 조회
            // alias 설정
            NumberPath<Long> postLikeCount = Expressions.numberPath(Long.class, "postLikeCount");
            return getPostListResJPAQuery(userId, challenge, review)
                    .orderBy(postLikeCount.desc())
                    .offset((page - 1) * size)
                    .limit(size)
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
                            post.id, user.nickname, user.profileImgUrl, post.content, post.challenge, post.review,
                            calculateWeeksAgo(post.createdAt), // 몇 주 전인지 계산
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
    private JPAQuery<PostListRes> getPostListResJPAQuery(Long userId, Boolean challenge, Boolean review) {
        return queryFactory
                .select(new QPostListRes(
                        post.id, post.content, post.challenge, post.review,
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
                .where(
                        post.status.eq(StatusType.ACTIVE),
                        challengeFilter(challenge),
                        reviewFilter(review)
                );
    }

    private static Predicate reviewFilter(Boolean review) {
        return review != null && review ? post.review.eq(true) : null;
    }

    private static Predicate challengeFilter(Boolean challenge) {
        return challenge != null && challenge ? post.challenge.eq(true) : null;
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

    // 몇 주 전인지 계산
    public static NumberTemplate<Integer> calculateWeeksAgo(DateTimePath<LocalDateTime> createdAt) {
        return Expressions.numberTemplate(Integer.class, "datediff({0}, {1})/7", currentTime(), createdAt);
    }

    /**
     * 내가 쓴 글 조회
     */
    public List<GetPostByUserRes> getPostListByUser(Long userId, Long page, Long size) {
        return queryFactory
                .select(new QGetPostByUserRes(
                        user.profileImgUrl, user.nickname, post.content, postLikeCount(), commentCount(), postImgCount(), bookmarked(userId))
                )
                .from(user)
                .leftJoin(post).on(user.id.eq(post.userId))
                .where(user.id.eq(userId), post.status.eq(StatusType.ACTIVE))
                .orderBy(post.createdAt.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();
    }

    // 글 이미지 개수 쿼리
    private JPQLQuery<Integer> postImgCount() {
        return select(postImage.count().intValue()).from(postImage).where(postImage.postId.eq(post.id), postImage.status.eq(StatusType.ACTIVE));
    }
}
