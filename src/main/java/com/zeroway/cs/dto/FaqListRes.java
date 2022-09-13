package com.zeroway.cs.dto;

import lombok.Data;

@Data
public class FaqListRes {

    private long id;
    private String question;

    public FaqListRes(long id, String question) {
        this.id = id;
        this.question = question;
    }
}
