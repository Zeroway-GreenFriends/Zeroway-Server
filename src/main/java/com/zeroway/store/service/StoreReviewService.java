package com.zeroway.store.service;

import com.zeroway.common.BaseException;
import com.zeroway.store.dto.CreateReviewReq;
import com.zeroway.store.dto.ReviewInfo;
import com.zeroway.store.entity.Store;
import com.zeroway.store.entity.StoreReview;
import com.zeroway.store.repository.StoreRepository;
import com.zeroway.store.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class StoreReviewService {

    private final StoreReviewRepository storeReviewRepository;
    private final StoreRepository storeRepository;

    // 상점 리뷰 추가
    @Transactional
    public Long createReview(CreateReviewReq req, Long userId) throws BaseException {
        try {
            // 이미 리뷰를 작성한 경우
            if (storeReviewRepository.findByStoreIdAndUserId(req.getStoreId(), userId).isPresent())
                throw new BaseException(ALREADY_WRITTEN);

            // 리뷰 추가
            Long reviewId = storeReviewRepository.save(
                    StoreReview.builder()
                            .storeId(req.getStoreId())
                            .userId(userId)
                            .score(req.getScore())
                            .content(req.getContent())
                            .build()).getId();

            // 평균 별점 갱신
            updateAvgScore(req.getStoreId());

            return reviewId;
        } catch (BaseException e) {
            throw e;
        }catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 제로웨이스트샵 전체 리뷰 조회
    public List<ReviewInfo> getAllReview(Long storeId, Long userId) throws BaseException {
        try {
            return storeReviewRepository.getReviewInfo(storeId, userId);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 제로웨이스트 샵 평균 별점 갱신
    private void updateAvgScore(Long storeId) throws BaseException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BaseException(INVALID_STORE_ID));
        List<StoreReview> storeReview = storeReviewRepository.findByStoreId(storeId);
        Double scoreSum = 0D;
        for (StoreReview review : storeReview) { scoreSum += review.getScore();}

        // 소수 둘째 자리까지 반올림하여 저장
        store.setScoreAvg(Math.round((scoreSum / storeReview.size() * 100))/100.0);
    }
}
