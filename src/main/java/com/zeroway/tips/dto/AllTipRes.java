package com.zeroway.tips.dto;

import com.zeroway.tips.entity.Term;
import com.zeroway.tips.entity.Tip;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllTipRes {

    private List<Tip> tips = new ArrayList<>();
    private List<Term> terms = new ArrayList<>();

    public AllTipRes(List<Tip> tips, List<Term> terms) {
        this.tips.addAll(tips);
        this.terms.addAll(terms);
    }
}
