package com.zeroway.cs.service;

import com.zeroway.common.BaseException;
import com.zeroway.cs.dto.QnaListRes;
import com.zeroway.cs.dto.QnaRes;
import com.zeroway.cs.entity.QnA;
import com.zeroway.cs.repository.QnaImageRepository;
import com.zeroway.cs.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.INVALID_QNA_ID;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final QnaImageRepository qnaImageRepository;

    public List<QnaListRes> getQnaList(Long userId) throws BaseException {
        try {
            List<QnA> qnaList = qnaRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));

            return qnaList.stream()
                    .map(qna -> new QnaListRes(qna.getId(), qna.getTypeOfQuestion().getName()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public QnaRes getQna(Long qnaId) throws BaseException{
        try {
            Optional<QnA> qna = qnaRepository.findById(qnaId);
            if(qna.isEmpty()) throw new BaseException(INVALID_QNA_ID);
            List<String> qnaImgList = qnaImageRepository.findUrlByQna_Id(qnaId);

            return new QnaRes(qna.get().getTypeOfQuestion().getName(),
                    qna.get().getQuestion(),
                    qnaImgList,
                    qna.get().getAnswer());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
