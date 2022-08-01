package com.zeroway.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LikeListRes {

    private int totalCount; // 좋아요 개수
    private List<UserInfo> likeList = new ArrayList<>(); // 회원 목록

    public LikeListRes(List<UserInfo> likeList) {
        this.totalCount = likeList.size();
        this.likeList.addAll(likeList);
    }
}
