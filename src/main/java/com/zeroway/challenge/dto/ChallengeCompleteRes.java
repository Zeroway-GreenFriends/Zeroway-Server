package com.zeroway.challenge.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ChallengeCompleteRes {
    private Integer level;

    public ChallengeCompleteRes(Integer level) {
        this.level = level;
    }
}


