package com.zeroway.store.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponseStatus;
import com.zeroway.store.dto.StoreListRes;
import com.zeroway.store.dto.StoreRes;
import com.zeroway.store.entity.Store;
import com.zeroway.store.repository.StoreRepository;
import com.zeroway.store.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreReviewRepository storeReviewRepository;

    public List<StoreListRes> searchStoreList(String keyword, int page, int size) throws BaseException {
        try{
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
            return storeRepository.findByAddressNewContains(keyword, pageRequest).getContent().stream()
                    .map(store -> new StoreListRes(store.getId(), store.getImageUrl(), store.getName(), store.getItem(), store.getScoreAvg(),
                            store.getAddressNew(), store.getOperatingTime(), store.getContact(), store.getSiteUrl(), store.getInstagram())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<StoreListRes> getStoreList(int page, int size) throws BaseException {
        try{
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("scoreAvg").descending());
            return storeRepository.findAll(pageRequest).getContent().stream()
                    .map(store -> new StoreListRes(store.getId(), store.getImageUrl(), store.getName(), store.getItem(), store.getScoreAvg(),
                            store.getAddressNew(), store.getOperatingTime(), store.getContact(), store.getSiteUrl(), store.getInstagram())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 제로웨이스트샵 상세 조회
    public StoreRes getStoreDetail(Long storeId, Long userId) throws BaseException  {
        try {
            return new StoreRes(
                    storeRepository.findById(storeId).orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_STORE_ID)),
                    storeReviewRepository.getReviewInfo(storeId, userId)
                );
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
