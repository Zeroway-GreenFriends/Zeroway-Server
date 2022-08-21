package com.zeroway.store.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zeroway.common.StatusType;
import com.zeroway.store.dto.QReviewInfo;
import com.zeroway.store.dto.ReviewInfo;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.zeroway.store.entity.QStoreReview.storeReview;
import static com.zeroway.store.entity.QStoreReviewLike.storeReviewLike;
import static com.zeroway.user.entity.QUser.user;

public class StoreReviewRepositoryImpl implements StoreReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public StoreReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 제로웨이스트샵 리뷰 리스트 전체 조회
    @Override
    public List<ReviewInfo> getReviewInfo(Long storeId, Long userId) {
        return getReviewInfoOrderByCreatedAt(storeId, userId)
                .fetch();
    }

    // 제로웨이스트샵 리뷰 리스트 조회 (limit)
    @Override
    public List<ReviewInfo> getReviewInfo(Long storeId, Long userId, int size) {
        return getReviewInfoOrderByCreatedAt(storeId, userId)
                .limit(size)
                .fetch();
    }

    private JPAQuery<ReviewInfo> getReviewInfoOrderByCreatedAt(Long storeId, Long userId) {
        return queryFactory
                .select(new QReviewInfo(
                        storeReview.id, user.nickname, user.profileImgUrl,
                        storeReview.score, storeReview.content,
                        select(storeReviewLike.isNotNull()).from(storeReviewLike).where(storeReviewEq(), storeReviewLikeActive(), storeReviewLike.userId.eq(userId)),
                        select(storeReviewLike.count().intValue()).from(storeReviewLike).where(storeReviewEq(), storeReviewLikeActive()),
                        storeReview.createdAt
                ))
                .from(storeReview)
                .join(user).on(storeReview.userId.eq(user.id))
                .where(
                        storeReview.storeId.eq(storeId)
                )
                .orderBy(storeReview.createdAt.desc());
    }

    private static BooleanExpression storeReviewLikeActive() {
        return storeReviewLike.status.eq(StatusType.ACTIVE);
    }

    private static BooleanExpression storeReviewEq() {
        return storeReviewLike.storeReviewId.eq(storeReview.id);
    }
}
