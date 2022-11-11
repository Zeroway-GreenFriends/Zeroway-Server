package com.zeroway.challenge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.zeroway.challenge.entity.QLevel.level;

public class LevelRepositoryImpl implements LevelRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LevelRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public long maxLevel() {
        return queryFactory.from(level).select(level.id.max()).fetchOne();
    }
}
