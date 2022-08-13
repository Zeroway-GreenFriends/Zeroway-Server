package com.zeroway.cs.dto;

import lombok.Data;

@Data
public class FaqListRes {

    private String question;
    private String answer;

    public FaqListRes(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
