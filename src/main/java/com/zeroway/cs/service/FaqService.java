package com.zeroway.cs.service;

import com.zeroway.common.BaseException;
import com.zeroway.cs.dto.FaqListRes;
import com.zeroway.cs.dto.FaqRes;
import com.zeroway.cs.entity.FAQ;
import com.zeroway.cs.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.INVALID_FAQ_ID;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    public List<FaqListRes> getFaqList() throws BaseException {
        try {
            List<FAQ> faqList = faqRepository.findAll();
            return faqList.stream()
                    .map(faq -> new FaqListRes(faq.getId(), faq.getQuestion()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public FaqRes getFaq(Long faqId) throws BaseException {
        try{
            FAQ faq = faqRepository.findById(faqId).orElseThrow(() -> new BaseException(INVALID_FAQ_ID));
            return new FaqRes(faq.getQuestion(), faq.getAnswer());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
