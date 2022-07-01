package com.zeroway.tips.repository;

import com.zeroway.tips.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
}
