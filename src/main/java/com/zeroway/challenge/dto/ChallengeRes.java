package com.zeroway.challenge.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Data
public class ChallengeRes {
    private String nickname;
    private Integer level;
    private Integer exp;
    private String imgUrl;

    public ChallengeRes(String nickname, Integer level, Integer exp, String imgUrl) {
        this.nickname = nickname;
        this.level = level;
        this.exp = exp;
        this.imgUrl = imgUrl;
    }

}


