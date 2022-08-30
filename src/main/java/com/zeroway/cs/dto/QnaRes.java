package com.zeroway.cs.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QnaRes {

    private String type;
    private String question;
    private String answer;

    public QnaRes(String type, String question, List<String> imgList, String answer) {
        this.type = type;
        this.question = question;
        this.answer = answer;
    }
}
