package com.zeroway.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetChallengeRes {
    private int level;
    private double exp;
    private List<GetChallengeListRes> challenges;

    public GetChallengeRes(int level, List<GetChallengeListRes> getChallengeListRes) {
        this.level = level;
        this.challenges = getChallengeListRes;
    }
}
