package com.zeroway.cs.repository;

import com.zeroway.cs.entity.qna.QnAImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnaImageRepository extends JpaRepository<QnAImage, Long> {

    @Query("select qi.url from QnAImage qi where qi.qna_id = :qnaId")
    List<String> findUrlByQna_Id(Long qnaId);
}
