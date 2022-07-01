package com.zeroway.challenge.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.user.entity.User;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
public class Challenge extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    private String content;

    // 챌린지 별 레벨
    private int level;





}
