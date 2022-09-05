package com.zeroway.cs.dto;

import lombok.Data;

@Data
public class QnaListRes {

    private Long id;
    private String type;

    public QnaListRes(Long id, String type) {
        this.id = id;
        this.type = type;
    }
}
