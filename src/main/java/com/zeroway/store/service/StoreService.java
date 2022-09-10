package com.zeroway.store.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponseStatus;
import com.zeroway.s3.S3Uploader;
import com.zeroway.store.dto.StoreListRes;
import com.zeroway.store.dto.StoreReq;
import com.zeroway.store.dto.StoreRes;
import com.zeroway.store.entity.RequestType;
import com.zeroway.store.entity.StoreRequest;
import com.zeroway.store.repository.StoreRepository;
import com.zeroway.store.repository.StoreRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.FILE_UPLOAD_ERROR;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreRequestRepository storeRequestRepository;
    private final S3Uploader s3Uploader;

    public List<StoreListRes> searchStoreList(String keyword, int page, int size) throws BaseException {
        try{
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
            return storeRepository.findByAddressNewContains(keyword, pageRequest).getContent().stream()
                    .map(store -> new StoreListRes(store.getId(), store.getImageUrl(), store.getName(), store.getItem(),
                            store.getAddressNew(), store.getOperatingTime(), store.getContact(), store.getSiteUrl(), store.getInstagram())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<StoreListRes> getStoreList(int page, int size) throws BaseException {
        try{
            PageRequest pageRequest = PageRequest.of(page, size);
            return storeRepository.findAll(pageRequest).getContent().stream()
                    .map(store -> new StoreListRes(store.getId(), store.getImageUrl(), store.getName(), store.getItem(),
                            store.getAddressNew(), store.getOperatingTime(), store.getContact(), store.getSiteUrl(), store.getInstagram())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 제로웨이스트샵 상세 조회
    public StoreRes getStoreDetail(Long storeId) throws BaseException  {
        try {
            return new StoreRes(storeRepository.findById(storeId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_STORE_ID)));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /**
     * 상점 삭제 요청
     */
    @Transactional
    public void deleteStoreRequest(Long storeId, MultipartFile image) throws BaseException {

        try {
            String imageUrl = s3Uploader.uploadFile(image, "storeRequestImage");
            System.out.println("imageUrl = " + imageUrl);
            storeRequestRepository.save(
                    StoreRequest.builder()
                            .storeId(storeId)
                            .type(RequestType.DELETE)
                            .imageUrl(imageUrl)
                            .build()
            );
        } catch (IOException e) {
            throw new BaseException(FILE_UPLOAD_ERROR);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /**
     * 상점 수정 요청
     */
    @Transactional
    public void updateStoreRequest(Long storeId, StoreReq req, MultipartFile image) throws BaseException {
        try {
            String imageUrl = null;
            if (image != null) imageUrl = s3Uploader.uploadFile(image, "storeRequestImage");
            storeRequestRepository.save(
                   new StoreRequest(storeId, req, imageUrl, RequestType.UPDATE)
            );
        } catch (IOException e) {
            throw new BaseException(FILE_UPLOAD_ERROR);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 상점 등록 요청
     */
    @Transactional
    public void addStoreRequest(Long storeId, StoreReq req, MultipartFile image) throws BaseException {
        try {
            String imageUrl = s3Uploader.uploadFile(image, "storeRequestImage");
            storeRequestRepository.save(
                    new StoreRequest(storeId, req, imageUrl, RequestType.ADD)
            );
        } catch (IOException e) {
            throw new BaseException(FILE_UPLOAD_ERROR);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
