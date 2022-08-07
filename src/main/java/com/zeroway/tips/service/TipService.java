package com.zeroway.tips.service;

import com.zeroway.common.BaseException;
import com.zeroway.tips.dto.TodayTipsRes;
import com.zeroway.tips.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipService {

    private final TipRepository tipRepository;

    @Transactional(readOnly = true)
    public List<TodayTipsRes> getRandomTips(long size) throws BaseException {
        List<Long> idList = new ArrayList<>();

        // 전체 tip 개수
        long totalCount = tipRepository.count();

        // 전체 개수가 size 보다 작은 경우
        // -> 모든 tip을 조회
        if (totalCount <= size) {
            return tipRepository.findAll().stream().map(tip -> new TodayTipsRes(tip.getTitle(), tip.getContent()))
                    .collect(Collectors.toList());
        }

        // 랜덤 id 생성
        for (long i=0; i<size; i++) {
            Long id = (long)(Math.random()*totalCount) + 1;
            while (idList.contains(id)){
                id = (long)(Math.random()*totalCount) + 1;
            }
            idList.add(id);
        }

        try {
            return tipRepository.findAllById(idList).stream()
                    .map(tip -> new TodayTipsRes(tip.getTitle(), tip.getContent()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
