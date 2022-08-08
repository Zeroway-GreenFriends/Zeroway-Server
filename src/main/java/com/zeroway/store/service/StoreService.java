package com.zeroway.store.service;

import com.zeroway.common.BaseException;
import com.zeroway.store.dto.StoreListRes;
import com.zeroway.store.repository.StoreRepository;
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

    public List<StoreListRes> searchStoreList(String keyword, int page, int size) throws BaseException {
        try{
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
            return storeRepository.findByAddressNewContains(keyword, pageRequest).getContent().stream()
                    .map(store -> new StoreListRes(store.getImageUrl(), store.getName(), store.getItem(), store.getScoreAvg(),
                            store.getAddressNew(), store.getOperatingTime(), store.getContact(), store.getSiteUrl(), store.getInstagram())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<StoreListRes> getStoreList(int page, int size) throws BaseException {
        try{
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("scoreAvg").descending());
            return storeRepository.findAll(pageRequest).getContent().stream()
                    .map(store -> new StoreListRes(store.getImageUrl(), store.getName(), store.getItem(), store.getScoreAvg(),
                            store.getAddressNew(), store.getOperatingTime(), store.getContact(), store.getSiteUrl(), store.getInstagram())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}