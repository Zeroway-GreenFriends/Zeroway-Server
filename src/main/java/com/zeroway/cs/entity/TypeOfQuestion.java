package com.zeroway.cs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

}
