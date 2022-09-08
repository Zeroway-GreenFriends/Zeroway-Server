package com.zeroway.cs.repository;

import com.zeroway.cs.entity.qna.QnA;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<QnA, Long> {

    List<QnA> findByUserId(Long userId, Sort createdAt);
}
