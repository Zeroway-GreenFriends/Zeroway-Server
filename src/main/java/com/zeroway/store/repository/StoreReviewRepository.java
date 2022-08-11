package com.zeroway.store.repository;

import com.zeroway.store.entity.StoreReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {
    List<StoreReview> findByStoreId(Long storeId);
    Optional<StoreReview> findByStoreIdAndUserId(Long storeId, Long userId);

}
