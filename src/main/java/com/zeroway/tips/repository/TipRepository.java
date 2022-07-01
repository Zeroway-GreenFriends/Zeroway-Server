package com.zeroway.tips.repository;

import com.zeroway.tips.entity.Tip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipRepository extends JpaRepository<Tip, Long> {
}
