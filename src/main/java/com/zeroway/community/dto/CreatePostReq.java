package com.zeroway.community.dto;

import lombok.Data;

@Data
public class CreatePostReq {
    private String content;
    private boolean challenge;
}
