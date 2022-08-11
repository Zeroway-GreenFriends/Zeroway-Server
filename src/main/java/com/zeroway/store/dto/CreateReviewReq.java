package com.zeroway.store.dto;

import lombok.Data;

@Data
public class CreateReviewReq {

    private Long storeId;   // 제로웨이스트 샵 id
    private String content; // 리뷰 내용
    private Double score;   // 별점 (0.5~5.0)
}
