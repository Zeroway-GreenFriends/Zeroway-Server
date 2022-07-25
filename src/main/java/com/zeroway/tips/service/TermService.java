package com.zeroway.tips.service;

import com.zeroway.common.BaseException;
import com.zeroway.tips.dto.TermRes;
import com.zeroway.tips.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;


    /**
     * 임의의 환경 용어 조회
     * @param size 데이터 개수
     */
    public List<TermRes> getTerm(int page, int size) throws BaseException {
        try {
            return termRepository.findAll(PageRequest.of(page, size)).getContent()
                    .stream().map(term -> new TermRes(term.getName(), term.getDescription())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 검색어로 환경 용어 조회
     * @param keyword 검색어
     * @param page 페이지
     * @param size 데이터 개수
     */
    public List<TermRes> searchTerm(String keyword, int page, int size) throws BaseException {
        try {
            return termRepository.findByNameContaining(keyword, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"))).getContent()
                    .stream().map(term -> new TermRes(term.getName(), term.getDescription())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
