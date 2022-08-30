package com.zeroway.cs.service;

import com.zeroway.common.BaseException;
import com.zeroway.cs.dto.QnaListRes;
import com.zeroway.cs.dto.QnaReq;
import com.zeroway.cs.dto.QnaRes;
import com.zeroway.cs.entity.QnA;
import com.zeroway.cs.entity.QnAImage;
import com.zeroway.cs.entity.TypeOfQuestion;
import com.zeroway.cs.repository.QnaImageRepository;
import com.zeroway.cs.repository.QnaRepository;
import com.zeroway.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final QnaImageRepository qnaImageRepository;
    private final S3Uploader s3Uploader;

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

    public void createQna(QnaReq qnaReq, Long userId) throws BaseException{
        try {
            TypeOfQuestion type = TypeOfQuestion.enumOf(qnaReq.getType());
            QnA qna = qnaRepository.save(QnA.builder()
                    .typeOfQuestion(type).question(qnaReq.getQuestion()).userId(userId).build());

        } catch (Exception e) {
            throw new BaseException(FILE_UPLOAD_ERROR);
        }
    }

    public void createQna(QnaReq qnaReq, Long userId, List<MultipartFile> imgs) throws BaseException{
        try {
            TypeOfQuestion type = TypeOfQuestion.enumOf(qnaReq.getType());
            QnA qna = qnaRepository.save(QnA.builder()
                    .typeOfQuestion(type).question(qnaReq.getQuestion()).userId(userId).build());

            if(imgs!=null && !imgs.isEmpty()) {
                for (String qnaUrl : s3Uploader.uploadFiles(imgs, "qnaImages")) {
                    qnaImageRepository.save(QnAImage.builder()
                            .qna_id(qna.getId()).url(qnaUrl).build());
                }
            }
        } catch (Exception e) {
            throw new BaseException(FILE_UPLOAD_ERROR);
        }
    }

}
