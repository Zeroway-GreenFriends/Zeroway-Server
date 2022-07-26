package com.zeroway.challenge.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Level {

    @Id @GeneratedValue
    @Column(name = "level_id")
    private Integer id;

    // 레벨별 캐릭터 이미지
    @Column(nullable = false, length = 100)
    private String imageUrl;
}
