package com.zeroway.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FaqRes {

    private String question;
    private String answer;

}
