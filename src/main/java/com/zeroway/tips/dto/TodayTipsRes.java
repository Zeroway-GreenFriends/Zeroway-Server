package com.zeroway.tips.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodayTipsRes {

    private String tipNum;
    private String title;
    private String content;
}
