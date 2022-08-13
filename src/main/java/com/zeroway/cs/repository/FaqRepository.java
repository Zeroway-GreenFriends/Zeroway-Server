package com.zeroway.cs.repository;

import com.zeroway.cs.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<FAQ, Long> {
}
