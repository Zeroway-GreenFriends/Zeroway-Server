package com.zeroway.community.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.zeroway.community.entity.QPostImage.postImage;

public class PostImageRepositoryImpl implements PostImageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostImageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<String> findUrlByPostId(Long postId) {
        return queryFactory
                .select(postImage.url)
                .from(postImage)
                .where(postImage.postId.eq(postId))
                .fetch();
    }
}
