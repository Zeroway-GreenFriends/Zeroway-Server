package com.zeroway.challenge.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RedisHash("challenge")
public class RedisChallenge {

    @Id
    Long userId;

    List<Challenge> challenge = new ArrayList<>();

    @TimeToLive Long expiration;

    @Getter
    @Setter
    public static class Challenge {
        Long id;
        Boolean complete;

        public Challenge(Long id, Boolean complete) {
            this.id = id;
            this.complete = complete;
        }
    }

    @Builder
    public RedisChallenge(Long userId, List<Long> challenge, Long expiration) {
        this.userId = userId;
        for (Long cid : challenge) {
            this.challenge.add(new Challenge(cid, false));
        }
        this.expiration = expiration;
    }
}
