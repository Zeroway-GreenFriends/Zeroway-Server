package com.zeroway.community.entity;

import com.zeroway.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private Long userId;

    @Column(length = 1000)
    private String content;

    @ColumnDefault("false")
    private boolean challenge; // 챌린지 인증 여부

    @Builder
    public Post(Long userId, String content, boolean challenge) {
        this.userId = userId;
        this.content = content;
        this.challenge = challenge;
    }
}
