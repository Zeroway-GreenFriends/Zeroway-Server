package com.zeroway.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class GetPostListByMypageRes {

    private String profileImgUrl;
    private String nickname;
    private String content;
    private int likeCount;
    private int commentCount;
    private int imgCount;
    private boolean isScraped;

    @QueryProjection
    public GetPostListByMypageRes(String profileImgUrl, String nickname, String content, int likeCount, int commentCount, int imgCount, boolean isScraped) {
        this.profileImgUrl = profileImgUrl;
        this.nickname = nickname;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.imgCount = imgCount;
        this.isScraped = isScraped;
    }
}
