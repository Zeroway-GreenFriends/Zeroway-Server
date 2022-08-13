package com.zeroway.cs.service;

import com.zeroway.common.BaseException;
import com.zeroway.cs.dto.QnaListRes;
import com.zeroway.cs.entity.QnA;
import com.zeroway.cs.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;

    public List<QnaListRes> getQnaList(Long userId) throws BaseException {
        try {

            List<QnA> qnaList = qnaRepository.findByUser_Id(userId, Sort.by(Sort.Direction.DESC, "createdAt"));

            return qnaList.stream()
                    .map(qna -> new QnaListRes(qna.getId(), qna.getTypeOfQuestion().getName()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
