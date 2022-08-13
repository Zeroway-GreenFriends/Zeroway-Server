package com.zeroway.cs.repository;

import com.zeroway.cs.entity.QnAImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QnaImageRepository extends JpaRepository<QnAImage, Long> {

    @Query("select qi.url from QnAImage qi join qi.qna q where q.id = :qnaId")
    List<String> findUrlByQna_Id(Long qnaId);
}
