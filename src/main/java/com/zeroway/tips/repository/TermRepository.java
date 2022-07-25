package com.zeroway.tips.repository;

import com.zeroway.tips.entity.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
    Page<Term> findByNameContaining(String name, Pageable pageable);
    Page<Term> findAll(Pageable pageable);
}
