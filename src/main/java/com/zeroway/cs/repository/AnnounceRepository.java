package com.zeroway.cs.repository;

import com.zeroway.cs.entity.Announce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {
}
