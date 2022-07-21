package com.zeroway.challenge.entity;

import com.zeroway.common.BaseEntity;

import javax.persistence.*;

@Entity
public class Challenge extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    private String content;

    // 챌린지 별 레벨
    @ManyToOne(optional = false)
    @JoinColumn(name = "level_id")
    private Level level;


}
