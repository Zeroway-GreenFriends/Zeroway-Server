package com.zeroway.cs.repository;

import com.zeroway.cs.entity.QnA;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<QnA, Long> {

    List<QnA> findByUser_Id(Long userId, Sort createdAt);
}
