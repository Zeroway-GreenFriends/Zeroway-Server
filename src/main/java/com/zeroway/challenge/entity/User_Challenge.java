package com.zeroway.challenge.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.user.entity.User;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
public class User_Challenge extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

    //챌린지 달성 여부
    @ColumnDefault("false")
    private boolean complete = false;






}
