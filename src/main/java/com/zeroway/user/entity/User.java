package com.zeroway.user.entity;

import com.zeroway.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    // 달성한 챌린지의 개수
    @ColumnDefault("0")
    @Builder.Default
    private Integer challengeCount = 0;

    // 캐릭터 레벨
    @ColumnDefault("1")
    @Builder.Default
    private Integer level = 1;

    public User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
