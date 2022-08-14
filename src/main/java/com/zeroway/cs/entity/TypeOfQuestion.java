package com.zeroway.cs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TypeOfQuestion {
    CHALLENGE("챌린지"),
    COMMUNITY("커뮤니티"),
    PROFILE("프로필"),
    NOTICE("알림"),
    DECLARATION("신고"),
    ETC("기타");

    private final String name;

    public static TypeOfQuestion enumOf(String name) {
        return Arrays.stream(TypeOfQuestion.values())
                .filter(t -> t.getName().equals(name))
                .findAny().orElse(null);
    }

}