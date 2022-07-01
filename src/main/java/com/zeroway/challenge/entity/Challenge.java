package com.zeroway.challenge.entity;

import com.zeroway.common.BaseEntity;

import javax.persistence.*;

@Entity
public class Challenge extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    //챌린지 달성 여부
    private boolean complete;

    private String content;

    // 챌린지 별 레벨
    private int level;





}
