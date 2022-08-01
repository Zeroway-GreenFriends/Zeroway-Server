package com.zeroway.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateLikeRes {
    private boolean like; // 좋아요 여부
}
