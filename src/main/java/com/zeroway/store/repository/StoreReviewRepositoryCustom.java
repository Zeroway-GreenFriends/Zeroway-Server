package com.zeroway.store.repository;

import com.zeroway.store.dto.ReviewInfo;

import java.util.List;

public interface StoreReviewRepositoryCustom {

    List<ReviewInfo> getReviewInfo(Long storeId, Long userId);
}
