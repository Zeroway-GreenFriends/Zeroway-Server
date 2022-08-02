package com.zeroway.challenge.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User_Challenge extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    //챌린지 달성 여부
    @ColumnDefault("false")
    @Builder.Default
    private boolean complete = false;

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Builder
    public User_Challenge(User user, Challenge challenge) {
        this.user = user;
        this.challenge = challenge;
    }
}
