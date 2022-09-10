package com.zeroway.store.repository;

import com.zeroway.store.entity.StoreRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRequestRepository extends JpaRepository<StoreRequest, Long> {
}
