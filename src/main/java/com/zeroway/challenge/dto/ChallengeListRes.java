package com.zeroway.challenge.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ChallengeListRes {
    private Long challengeId;
    private String content;
    private boolean complete;

    public ChallengeListRes(Long challengeId, String content, boolean complete) {
        this.challengeId = challengeId;
        this.content = content;
        this.complete = complete;
    }
}
