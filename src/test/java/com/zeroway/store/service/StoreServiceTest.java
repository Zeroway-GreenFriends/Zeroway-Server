package com.zeroway.store.service;

import com.zeroway.common.BaseException;
import com.zeroway.store.dto.StoreRes;
import com.zeroway.store.entity.Store;
import com.zeroway.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StoreServiceTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    StoreService storeService;

    @Test
    @DisplayName("제로웨이스트샵 상세 조회")
    void 상점_상세조회() throws BaseException {
        //given
        Store savedStore = storeRepository.save(new Store("상점", "도로명 주소", "구주소",
                "010-1234-5678", null, null, null,
                null, "다회용품", "설명")
        );

        //when
        StoreRes storeDetail = storeService.getStoreDetail(savedStore.getId());

        //then
        assertThat(storeDetail.getName()).isEqualTo("상점");
        assertThat(storeDetail.getAddressNew()).isEqualTo("도로명 주소");
        assertThat(storeDetail.getItem()).isEqualTo("다회용품");
        assertThat(storeDetail.getInstagram()).isEqualTo(null);
        assertThat(storeDetail.getDescription()).isEqualTo("설명");

    }

}