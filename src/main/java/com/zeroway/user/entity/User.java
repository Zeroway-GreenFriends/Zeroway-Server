package com.zeroway.user.entity;

import com.zeroway.common.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    // 달성한 챌린지의 개수
    private int challengeCount;

    // 캐릭터 레벨
    private int level;





}
