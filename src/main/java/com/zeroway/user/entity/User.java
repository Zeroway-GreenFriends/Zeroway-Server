package com.zeroway.user.entity;

import com.zeroway.challenge.entity.Level;
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

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private ProviderType provider;

    private String profileImgUrl;

    private String refreshToken;

    // 캐릭터 레벨
    @ManyToOne(optional = false)
    @JoinColumn(name = "level_id")
    private Level level;


    // 경험치
    @ColumnDefault("0")
    @Builder.Default
    private Integer exp = 0;

    public User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
