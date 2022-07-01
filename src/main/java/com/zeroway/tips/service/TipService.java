package com.zeroway.tips.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponseStatus;
import com.zeroway.tips.dto.AllTipRes;
import com.zeroway.tips.entity.Term;
import com.zeroway.tips.entity.Tip;
import com.zeroway.tips.repository.TermRepository;
import com.zeroway.tips.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipService {

    private final TipRepository tipRepository;
    private final TermRepository termRepository;

    @Transactional(readOnly = true)
    public AllTipRes getAllTips() throws BaseException {
        try{
            List<Tip> tips = tipRepository.findAll();
            for (Tip tip : tips) {
                System.out.println("tip = " + tip);
            }
            List<Term> terms = termRepository.findAll();
            return new AllTipRes(tips, terms);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
